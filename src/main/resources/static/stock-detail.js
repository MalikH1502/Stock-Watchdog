const params = new URLSearchParams(window.location.search);
const stockId = params.get("id");

const symbolHeading = document.getElementById("detail-symbol");
const typeBadge = document.getElementById("detail-type-badge");
const regionEl = document.getElementById("detail-region");
const companyNameEl = document.getElementById("detail-company-name");
const symbolFactEl = document.getElementById("detail-symbol-fact");
const priceEl = document.getElementById("detail-price");
const errorEl = document.getElementById("detail-error");

const ARROW_UP_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>alt-arrow-up-bold</title><path fill="#26ab28" d="m12.37 8.165l6.43 6.63c.401.414.158 1.205-.37 1.205H5.57c-.528 0-.771-.79-.37-1.205l6.43-6.63a.5.5 0 0 1 .74 0"/></svg>`;
const ARROW_DOWN_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>alt-arrow-down-bold</title><path fill="#8b0909" d="m12.37 15.835l6.43-6.63C19.201 8.79 18.958 8 18.43 8H5.57c-.528 0-.771.79-.37 1.205l6.43 6.63c.213.22.527.22.74 0"/></svg>`;


function formatRelativeTime(isoString) {
    if (!isoString) return null;
    const diffMin = Math.floor((Date.now() - new Date(isoString).getTime()) / 60000);

    if (diffMin < 1) return "just now";
    if (diffMin === 1) return "1 minute ago";
    if (diffMin < 60) return `${diffMin} minutes ago`;
    const diffHr = Math.floor(diffMin / 60);
    return diffHr === 1 ? "1 hour ago" : `${diffHr} hours ago`;
}
function getPriceChangeIndicator(stock) {
    if (stock.previousPrice == null || stock.price == null || stock.price === stock.previousPrice) {
        return { priceClass: '', arrowHtml: '' };
    }
    return stock.price > stock.previousPrice
        ? { priceClass: 'price-up', arrowHtml: ARROW_UP_SVG }
        : { priceClass: 'price-down', arrowHtml: ARROW_DOWN_SVG };
}

async function loadStockDetail() {
    if (!stockId) {
        errorEl.textContent = "No stock selected.";
        errorEl.style.display = "block";
        return;
    }

    try {
        const response = await fetch(`/api/stocks/${stockId}`);
        if (!response.ok) {
            throw new Error(`Request failed: ${response.status}`);
        }
        const stock = await response.json();

        if (!stock || !stock.symbol) {
            errorEl.textContent = "Stock not found.";
            errorEl.style.display = "block";
            return;
        }

        symbolHeading.textContent = stock.symbol;
        typeBadge.textContent = stock.type ?? '';
        regionEl.textContent = `${getRegionFlag(stock.region)} ${stock.region ?? ''}`.trim(); companyNameEl.textContent = stock.companyName ?? '';
        symbolFactEl.textContent = stock.symbol;

        if (stock.price != null) {
            const { priceClass, arrowHtml } = getPriceChangeIndicator(stock);
            priceEl.textContent = `$${stock.price.toFixed(2)}`;
            priceEl.classList.remove("placeholder");
            priceEl.classList.add(priceClass);
            priceEl.insertAdjacentHTML("beforeend", arrowHtml);

            const relTime = formatRelativeTime(stock.lastUpdated);
            if (relTime) {
                const updatedEl = document.createElement("div");
                updatedEl.className = "detail-price-updated";
                updatedEl.textContent = `Last updated ${relTime}`;
                priceEl.insertAdjacentElement("afterend", updatedEl);
            }
        } else {
            priceEl.textContent = "Price unavailable";
        }

        checkStockAlerts();
    } catch (error) {
        errorEl.textContent = `Error: ${error.message}`;
        errorEl.style.display = "block";
    }
}

const backButton = document.getElementById("back-button");
if (backButton) {
    backButton.addEventListener("click", () => {
        if (window.history.length > 1) {
            window.history.back();
        } else {
            window.location.href = "index.html";
        }
    });
}
async function checkStockAlerts() {
    if (!stockId) return;

    try {
        const response = await fetch("/api/alerts");
        if (!response.ok) return;
        const alerts = await response.json();
        const relevant = Array.isArray(alerts)
            ? alerts.filter(alert => String(alert.stock?.id) === String(stockId))
            : [];
        await checkAlertsAndNotify(relevant);
    } catch {
        // best-effort, a failed notification check shouldn't break the page
    }
}


function showAlertFeedback(message, type) {
    const alertFeedback = document.getElementById("alert-feedback");
    if (!alertFeedback) return;
    alertFeedback.textContent = message;
    alertFeedback.hidden = false;
    alertFeedback.className = `alert-feedback ${type}`;
}

const alertForm = document.getElementById("alert-form");
if (alertForm) {
    alertForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        if (!stockId) return;

        const condition = document.getElementById("alert-condition").value;
        const value = document.getElementById("alert-value").value;

        if (!value || Number(value) <= 0) {
            showAlertFeedback("Enter a valid price.", "error");
            return;
        }

        const csrfToken = await getCsrfToken();

        try {
            const response = await fetch("/api/alerts", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {})
                },
                body: JSON.stringify({
                    stockId: Number(stockId),
                    condition,
                    value: Number(value)
                })
            });

            if (!response.ok) {
                throw new Error(`Request failed: ${response.status}`);
            }

            showAlertFeedback("Alert set.", "success");
            alertForm.reset();
        } catch (error) {
            showAlertFeedback(`Error: ${error.message}`, "error");
        }
    });
}

loadStockDetail();