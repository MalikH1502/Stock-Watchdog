async function getCsrfToken() {
    try {
        const response = await fetch("/api/csrf");
        const data = await response.json();
        return data.token;
    } catch {
        return null;
    }
}

async function markAlertFired(alertId) {
    const csrfToken = await getCsrfToken();

    try {
        const response = await fetch(`/api/alerts/${alertId}/mark-fired`, {
            method: "POST",
            headers: {
                ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {})
            }
        });
        return response.ok;
    } catch {
        return false;
    }
}

function isAlertConditionMet(alert) {
    const price = alert.stock?.price;
    if (price == null || alert.value == null) return false;

    if (alert.condition === "ABOVE") return price >= alert.value;
    if (alert.condition === "BELOW") return price <= alert.value;
    return false;
}

function sendDesktopNotification(alert) {
    if (typeof Notification === "undefined" || Notification.permission !== "granted") return;

    const symbol = alert.stock?.symbol ?? "Stock";
    const conditionText = alert.condition === "ABOVE" ? "above" : "below";

    new Notification(`${symbol} alert triggered`, {
        body: `${symbol} is now ${conditionText} $${alert.value}`
    });
}

async function checkAlertsAndNotify(alerts) {
    if (!Array.isArray(alerts)) return;

    for (const alert of alerts) {
        if (alert.isTrue) continue;
        if (!isAlertConditionMet(alert)) continue;

        sendDesktopNotification(alert);
        alert.isTrue = true; // optimistic, avoids double-firing again this session while the server call is in flight
        await markAlertFired(alert.id);
    }
}

function updateNotificationStatus() {
    const statusEl = document.getElementById("notification-status");
    const button = document.getElementById("enable-notifications-btn");
    if (!statusEl || !button) return;

    if (typeof Notification === "undefined") {
        statusEl.textContent = "Desktop notifications aren't supported in this browser.";
        button.hidden = true;
        return;
    }

    if (Notification.permission === "granted") {
        statusEl.textContent = "Desktop notifications are enabled.";
        button.hidden = true;
    } else if (Notification.permission === "denied") {
        statusEl.textContent = "Desktop notifications are blocked. Enable them in your browser's site settings.";
        button.hidden = true;
    } else {
        statusEl.textContent = "Desktop notifications are off.";
        button.hidden = false;
    }
}

const enableNotificationsBtn = document.getElementById("enable-notifications-btn");
if (enableNotificationsBtn) {
    updateNotificationStatus();
    enableNotificationsBtn.addEventListener("click", async () => {
        if (typeof Notification === "undefined") return;
        await Notification.requestPermission();
        updateNotificationStatus();
    });
}