const params = new URLSearchParams(window.location.search);
const stockId = params.get("id");

const symbolHeading = document.getElementById("detail-symbol");
const typeBadge = document.getElementById("detail-type-badge");
const regionEl = document.getElementById("detail-region");
const companyNameEl = document.getElementById("detail-company-name");
const symbolFactEl = document.getElementById("detail-symbol-fact");
const priceEl = document.getElementById("detail-price");
const errorEl = document.getElementById("detail-error");

function formatRelativeTime(isoString) {
    if (!isoString) return null;
    const diffMin = Math.floor((Date.now() - new Date(isoString).getTime()) / 60000);

    if (diffMin < 1) return "just now";
    if (diffMin === 1) return "1 minute ago";
    if (diffMin < 60) return `${diffMin} minutes ago`;
    const diffHr = Math.floor(diffMin / 60);
    return diffHr === 1 ? "1 hour ago" : `${diffHr} hours ago`;
}

async function loadStockDetail() {
    if (!stockId) {
        errorEl.textContent = "No stock selected.";
        errorEl.style.display = "block";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/stocks/${stockId}`);
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
regionEl.textContent = `${getRegionFlag(stock.region)} ${stock.region ?? ''}`.trim();        companyNameEl.textContent = stock.companyName ?? '';
        symbolFactEl.textContent = stock.symbol;

        if (stock.price != null) {
            priceEl.textContent = `$${stock.price.toFixed(2)}`;
            priceEl.classList.remove("placeholder");

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

loadStockDetail();