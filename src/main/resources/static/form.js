(function () {
    const els = {
        searchInput: document.getElementById('stock-search'),
        resultsContainer: document.getElementById('results-container'),
        stockForm: document.getElementById('search-form'),
        spinner: document.getElementById('search-spinner')
    };

    let debounceTimer = null;
    let lastQuery = '';

    function qs(selector) {
        return document.querySelectorAll(selector);
    }

    async function fetchJson(url, opts = {}) {
        const { throwOnNotOk = true } = opts;
        const resp = await fetch(url);
        if (!resp.ok && throwOnNotOk) throw new Error(`Request failed: ${resp.status}`);
        return resp.json();
    }

    function showSpinner() {
        els.spinner?.classList.add('active');
        if (els.resultsContainer) els.resultsContainer.innerHTML = '';
    }

    function hideSpinner() {
        els.spinner?.classList.remove('active');
    }

    function escapeHtml(str) {
        const d = document.createElement('div');
        d.textContent = str;
        return d.innerHTML;
    }
function formatShortRelativeTime(isoString) {
    if (!isoString) return null;
    const diffMin = Math.floor((Date.now() - new Date(isoString).getTime()) / 60000);
    if (diffMin < 1) return "just now";
    if (diffMin < 60) return `${diffMin}m`;
    const diffHr = Math.floor(diffMin / 60);
    if (diffHr < 24) return `${diffHr}h`;
    const diffDay = Math.floor(diffHr / 24);
    return `${diffDay}d`;
}

function getStalenessClass(isoString) {
    if (!isoString) return "stale-unknown";
    const diffMin = (Date.now() - new Date(isoString).getTime()) / 60000;
    if (diffMin < 5) return "stale-fresh";
    if (diffMin < 30) return "stale-warning";
    return "stale-old";
}

function buildPriceBlockHtml(stock) {
    if (stock.price == null) return '';
    const updatedText = formatShortRelativeTime(stock.lastUpdated) ?? '';
    const stalenessClass = getStalenessClass(stock.lastUpdated);
    return `
            <div class="stock-card-price-block">
                <span class="stock-card-price">$${stock.price.toFixed(2)}</span>
                <span class="stock-card-updated ${stalenessClass}">${updatedText}</span>
            </div>`;
}

function buildStockCardsHtml(data) {
    return data.map(stock => `
        <div class="stock-card" data-id="${stock.id}">
            <span class="symbol">${stock.symbol}</span>
            <span class="company-name">${stock.companyName ?? ''}</span>
            <div class="meta">
                <span class="badge badge-type">${stock.type ?? ''}</span>
<span class="badge">${getRegionFlag(stock.region)} ${stock.region ?? ''}</span>            </div>${buildPriceBlockHtml(stock)}
        </div>
    `).join('');
}

   async function loadDashboardDefaults() {
    await updateTotalTrackedStat();
    await loadAlerts();
}
async function updateTotalTrackedStat() {
    const totalStat = document.getElementById("stat-total-tracked");
    if (!totalStat) return;

    try {
        const response = await fetch("http://localhost:8080/api/stocks/tracked-count");
        if (!response.ok) throw new Error(`Request failed: ${response.status}`);
        const count = await response.json();
        totalStat.textContent = count;
    } catch (error) {
        totalStat.textContent = "-";
    }
}

    async function loadAlerts() {
    const alertsList = document.getElementById("alerts-list");
    const alertsStat = document.getElementById("stat-alerts-set");

    try {
        const response = await fetch("http://localhost:8080/api/alerts");
        if (!response.ok) throw new Error(`Request failed: ${response.status}`);
        const alerts = await response.json();
        const list = Array.isArray(alerts) ? alerts : [];

        if (alertsStat) alertsStat.textContent = list.length;

        if (alertsList) {
            alertsList.innerHTML = list.length === 0
                ? `<p class="alerts-empty">No alerts set yet.</p>`
                : list.map(alert => `
                    <div class="alert-row">
                        <span class="alert-symbol">${alert.stock?.symbol ?? ''}</span>
                        <span class="alert-condition">${alert.condition ?? ''} $${alert.value ?? ''}</span>
                    </div>
                `).join('');
        }

        renderAlertStocks(list);
    } catch (error) {
        const message = `<p style="color:red">Error loading alerts: ${error.message}</p>`;
        if (alertsList) alertsList.innerHTML = message;
        if (els.resultsContainer) els.resultsContainer.innerHTML = message;
    }
}

async function loadAlerts() {
    const alertsList = document.getElementById("alerts-list");
    const alertsStat = document.getElementById("stat-alerts-set");

    try {
        const response = await fetch("http://localhost:8080/api/alerts");
        if (!response.ok) throw new Error(`Request failed: ${response.status}`);
        const alerts = await response.json();
        const list = Array.isArray(alerts) ? alerts : [];

        if (alertsStat) alertsStat.textContent = list.length;

        if (alertsList) {
            alertsList.innerHTML = list.length === 0
                ? `<p class="alerts-empty">No alerts set yet.</p>`
                : list.map(alert => `
                    <div class="alert-row">
                        <span class="alert-symbol">${alert.stock?.symbol ?? ''}</span>
                        <span class="alert-condition">${alert.condition ?? ''} $${alert.value ?? ''}</span>
                    </div>
                `).join('');
        }

        renderAlertStocks(list);
        await checkAlertsAndNotify(list);
    } catch (error) {
        const message = `<p style="color:red">Error loading alerts: ${error.message}</p>`;
        if (alertsList) alertsList.innerHTML = message;
        if (resultsContainer) resultsContainer.innerHTML = message;
    }
}

function renderAlertStocks(alerts) {
    if (!els.resultsContainer) return;

    const seen = new Set();
    const uniqueStocks = [];
    alerts.forEach(alert => {
        const stock = alert.stock;
        if (stock && stock.id != null && !seen.has(stock.id)) {
            seen.add(stock.id);
            uniqueStocks.push(stock);
        }
    });
    els.resultsContainer.innerHTML = uniqueStocks.length === 0
    ? `<p class="no-results">No alerts set yet. Search for a symbol and set an alert to see it here.</p>`
    : buildStockCardsHtml(uniqueStocks);
}

    async function performSearch(query) {
        const searchParams = new URLSearchParams({ query });
        const url = `http://localhost:8080/api/stocks/search?${searchParams}`;

        try {
            const data = await fetchJson(url, { throwOnNotOk: false });

            if (Array.isArray(data)) {
                els.resultsContainer.innerHTML = data.length === 0
                    ? `<p class="no-results">No results for "${escapeHtml(query)}". Try a different symbol or company name.</p>`
                    : buildStockCardsHtml(data);
            } else {
                els.resultsContainer.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
            }
        } catch (err) {
            els.resultsContainer.innerHTML = `<p style="color:red">Error: ${err.message}</p>`;
        } finally {
            hideSpinner();
        }
    }

    function setupEventListeners() {
        if (els.searchInput) {
            els.searchInput.value = '';
            els.searchInput.addEventListener('input', () => {
                const query = els.searchInput.value.trim();
                clearTimeout(debounceTimer);

                if (query.length < 2) {
                    hideSpinner();
                    if (els.resultsContainer) els.resultsContainer.innerHTML = '';
                    lastQuery = '';
                    return;
                }

                showSpinner();

                debounceTimer = setTimeout(() => {
                    if (query === lastQuery) {
                        hideSpinner();
                        return;
                    }
                    lastQuery = query;
                    performSearch(query);
                }, 2000);
            });
        }

        if (els.stockForm) {
            els.stockForm.addEventListener('submit', (e) => e.preventDefault());
        }

        if (els.resultsContainer) {
            els.resultsContainer.innerHTML = '';
            els.resultsContainer.addEventListener('click', (event) => {
                const card = event.target.closest('.stock-card');
                if (card && card.dataset.id) {
                    window.location.href = `stock-detail.html?id=${card.dataset.id}`;
                }
            });
        }

        const navButtons = qs('.tab-container button[data-page]');
        if (navButtons.length > 0) setupNavButtons(navButtons);
    }

    function setupNavButtons(buttons) {
        const currentPath = globalThis.location.pathname.replace(/\/+$/, '') || '/';
        buttons.forEach((button) => {
            const targetPath = new URL(button.dataset.page, globalThis.location.href).pathname.replace(/\/+$/, '') || '/';
            const row = button.closest('li');
            if (currentPath === targetPath && row) row.classList.add('active');
            button.addEventListener('click', () => globalThis.location.href = button.dataset.page);
        });
    }

    function onPageShow() {
        clearTimeout(debounceTimer);
        if (els.searchInput) els.searchInput.value = '';
        if (els.resultsContainer) els.resultsContainer.innerHTML = '';
        lastQuery = '';
        hideSpinner();
        loadDashboardDefaults();
    }

    // Init
    window.addEventListener('pageshow', onPageShow);
    setupEventListeners();
    // ensure initial load when script runs
    if (document.readyState === 'complete') onPageShow();
    else window.addEventListener('load', onPageShow);
})();