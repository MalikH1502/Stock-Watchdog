const params = new URLSearchParams(window.location.search);
const stockId = params.get("id");

const symbolHeading = document.getElementById("detail-symbol");
const typeBadge = document.getElementById("detail-type-badge");
const regionEl = document.getElementById("detail-region");
const companyNameEl = document.getElementById("detail-company-name");
const symbolFactEl = document.getElementById("detail-symbol-fact");
const errorEl = document.getElementById("detail-error");

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
        regionEl.textContent = stock.region ?? '';
        companyNameEl.textContent = stock.companyName ?? '';
        symbolFactEl.textContent = stock.symbol;
    } catch (error) {
        errorEl.textContent = `Error: ${error.message}`;
        errorEl.style.display = "block";
    }
}

loadStockDetail();