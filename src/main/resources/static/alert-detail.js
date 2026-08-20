(function () {
    const params = new URLSearchParams(window.location.search);
    const alertId = Number(params.get('id'));
    const els = {
        content: document.getElementById('alert-content'),
        message: document.getElementById('alert-message'),
        status: document.getElementById('alert-status'),
        title: document.getElementById('alert-title'),
        description: document.getElementById('alert-description'),
        id: document.getElementById('alert-id'),
        currentPrice: document.getElementById('current-price'),
        targetPrice: document.getElementById('target-price'),
        distance: document.getElementById('price-distance'),
        progress: document.getElementById('alert-progress'),
        activityStatus: document.getElementById('activity-status'),
        stockLink: document.getElementById('stock-link'),
        companyName: document.getElementById('company-name'),
        condition: document.getElementById('alert-condition'),
        region: document.getElementById('stock-region'),
        type: document.getElementById('stock-type'),
        updated: document.getElementById('stock-updated'),
        deleteButton: document.getElementById('delete-alert-btn')
    };

    let currentAlert = null;

    function formatPrice(value) {
        return value == null ? 'Pending' : `$${Number(value).toFixed(2)}`;
    }

    function formatUpdated(value) {
        if (!value) return 'Not available';
        const date = new Date(value);
        return Number.isNaN(date.getTime()) ? 'Not available' : date.toLocaleString();
    }

    function showMessage(message) {
        els.content.hidden = true;
        els.deleteButton.hidden = true;
        els.message.textContent = message;
        els.message.hidden = false;
    }

    function renderAlert(alert) {
        const stock = alert.stock ?? {};
        const target = Number(alert.value);
        const current = stock.price == null ? null : Number(stock.price);
        const isTriggered = Boolean(alert.isTrue);
        const conditionText = alert.condition === 'ABOVE' ? 'Price above' : 'Price below';
        const distance = current == null ? null : current - target;
        let progress = 0;
        if (current != null && target) {
            progress = alert.condition === 'ABOVE' ? (current / target) * 100 : (target / current) * 100;
        }
        let distanceText = 'Pending';
        if (distance != null) {
            const distanceSign = distance >= 0 ? '+' : '';
            distanceText = `${distanceSign}${formatPrice(distance)}`;
        }

        els.status.textContent = isTriggered ? 'Triggered' : 'Active';
        els.status.className = `status-badge ${isTriggered ? 'status-triggered' : 'status-active'}`;
        els.title.textContent = `${stock.symbol ?? 'Stock'} ${conditionText} ${formatPrice(target)}`;
        els.description.textContent = `Trigger when ${stock.symbol ?? 'the stock'} last price crosses ${alert.condition === 'ABOVE' ? 'above' : 'below'} ${formatPrice(target)}.`;
        els.id.textContent = `Alert ID: ${alert.id}`;
        els.currentPrice.textContent = formatPrice(current);
        els.targetPrice.textContent = formatPrice(target);
        els.distance.textContent = distanceText;
        els.progress.style.width = `${Math.min(100, Math.max(0, progress)).toFixed(0)}%`;
        els.progress.classList.toggle('triggered', isTriggered);
        els.activityStatus.textContent = isTriggered ? 'Triggered' : 'Active and monitoring';
        els.stockLink.textContent = stock.symbol ?? 'Unknown';
        els.stockLink.href = `stock-detail.html?id=${stock.id}`;
        els.companyName.textContent = stock.companyName ?? 'Not available';
        els.condition.textContent = conditionText;
        els.region.textContent = stock.region ?? 'Not available';
        els.type.textContent = stock.type ?? 'Not available';
        els.updated.textContent = formatUpdated(stock.lastUpdated);
        els.content.hidden = false;
        els.message.hidden = true;
        els.deleteButton.hidden = false;
    }

    async function loadAlert() {
        if (!Number.isInteger(alertId) || alertId <= 0) {
            showMessage('No valid alert was selected.');
            return;
        }

        try {
            const response = await fetch('/api/alerts');
            if (!response.ok) throw new Error(`Request failed: ${response.status}`);
            const alerts = await response.json();
            currentAlert = alerts.find(alert => Number(alert.id) === alertId);
            if (!currentAlert) {
                showMessage('Alert not found.');
                return;
            }
            renderAlert(currentAlert);
        } catch (error) {
            showMessage(`Error loading alert: ${error.message}`);
        }
    }

    async function deleteAlert() {
        if (!currentAlert || !window.confirm('Delete this alert?')) return;

        const csrfToken = typeof getCsrfToken === 'function' ? await getCsrfToken() : null;
        try {
            const response = await fetch(`/api/alerts/${currentAlert.id}`, {
                method: 'DELETE',
                headers: csrfToken ? { 'X-CSRF-TOKEN': csrfToken } : {}
            });
            if (!response.ok) throw new Error(`Request failed: ${response.status}`);
            window.location.href = 'alerts.html';
        } catch (error) {
            showMessage(`Error deleting alert: ${error.message}`);
        }
    }

    document.getElementById('back-button').addEventListener('click', () => {
        if (window.history.length > 1) window.history.back();
        else window.location.href = 'alerts.html';
    });
    els.deleteButton.addEventListener('click', deleteAlert);
    loadAlert();
})();
