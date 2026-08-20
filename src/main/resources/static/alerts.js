(function () {
    const els = {
        grid: document.getElementById('alerts-grid'),
        empty: document.getElementById('alerts-empty'),
        tabs: document.querySelectorAll('.tab'),
        countActive: document.getElementById('tab-count-active'),
        countTriggered: document.getElementById('tab-count-triggered'),
        countAll: document.getElementById('tab-count-all'),
        openModalBtn: document.getElementById('open-add-alert-btn'),
        closeModalBtn: document.getElementById('close-add-alert-btn'),
        modal: document.getElementById('add-alert-modal'),
        searchInput: document.getElementById('alert-stock-search'),
        searchResults: document.getElementById('stock-search-results'),
        selectedChip: document.getElementById('selected-stock-chip'),
        selectedLabel: document.getElementById('selected-stock-label'),
        changeStockBtn: document.getElementById('change-stock-btn'),
        newAlertForm: document.getElementById('new-alert-form'),
        newAlertFeedback: document.getElementById('new-alert-feedback')
    };

    let allAlerts = [];
    let activeTab = 'active';
    let selectedStock = null;
    let searchDebounce = null;

    function computeProgress(alert) {
        const price = alert.stock?.price;
        if (price == null || !alert.value) return null;

        const pct = alert.condition === 'ABOVE'
            ? (price / alert.value) * 100
            : (alert.value / price) * 100;

        return Math.min(100, Math.max(0, pct));
    }

    function buildAlertCardHtml(alert) {
        const progress = computeProgress(alert);
        const statusClass = alert.isTrue ? 'status-triggered' : 'status-active';
        const statusText = alert.isTrue ? 'Triggered' : 'Active';
        const currentPriceText = alert.stock?.price != null ? `$${alert.stock.price.toFixed(2)}` : 'Pending';

        const progressHtml = progress != null
            ? `<div class="alert-progress-track"><div class="alert-progress-fill ${alert.isTrue ? 'status-triggered-fill' : ''}" style="width:${progress.toFixed(0)}%;"></div></div>`
            : `<p class="panel-placeholder-text" style="margin:4px 0 0;">No live price cached for this stock yet. View it on the detail page to fetch one.</p>`;

        return `
        <div class="alert-card" data-id="${alert.id}">
            <div class="alert-card-header">
                <div>
                    <span class="alert-card-symbol">${alert.stock?.symbol ?? ''}</span>
                    <span class="alert-card-company">${alert.stock?.companyName ?? ''}</span>
                </div>
                <span class="status-badge ${statusClass}">${statusText}</span>
            </div>
            <div class="alert-card-details">
                <div>
                    <div class="alert-detail-label">Condition</div>
                    <div class="alert-detail-value">${alert.condition === 'ABOVE' ? 'Price Above' : 'Price Below'}</div>
                </div>
                <div>
                    <div class="alert-detail-label">Target</div>
                    <div class="alert-detail-value">$${alert.value?.toFixed(2) ?? ''}</div>
                </div>
            </div>
            <div>
                <div class="alert-detail-label">Current</div>
                <div class="alert-detail-value">${currentPriceText}</div>
                ${progressHtml}
            </div>
            <div class="alert-card-footer">
                <button type="button" class="alert-delete-btn" data-delete-id="${alert.id}" aria-label="Delete alert">Delete</button>
            </div>
        </div>`;
    }

    function updateCounts() {
        const activeCount = allAlerts.filter(a => !a.isTrue).length;
        const triggeredCount = allAlerts.filter(a => a.isTrue).length;
        els.countActive.textContent = activeCount;
        els.countTriggered.textContent = triggeredCount;
        els.countAll.textContent = allAlerts.length;
    }

    function renderAlerts() {
        updateCounts();
        const filtered = activeTab === 'all'
            ? allAlerts
            : allAlerts.filter(a => activeTab === 'triggered' ? a.isTrue : !a.isTrue);

        if (filtered.length === 0) {
            els.grid.innerHTML = '';
            els.empty.hidden = false;
        } else {
            els.empty.hidden = true;
            els.grid.innerHTML = filtered.map(buildAlertCardHtml).join('');
        }
    }

    async function fetchAlerts() {
        try {
            const response = await fetch('/api/alerts');
            if (!response.ok) throw new Error(`Request failed: ${response.status}`);
            allAlerts = await response.json();
            if (typeof checkAlertsAndNotify === 'function') {
                await checkAlertsAndNotify(allAlerts);
            }
            renderAlerts();
        } catch (error) {
            els.grid.innerHTML = `<p style="color:red">Error loading alerts: ${error.message}</p>`;
        }
    }

    async function deleteAlert(id) {
        const csrfToken = await getCsrfToken();
        try {
            const response = await fetch(`/api/alerts/${id}`, {
                method: 'DELETE',
                headers: { ...(csrfToken ? { 'X-CSRF-TOKEN': csrfToken } : {}) }
            });
            if (!response.ok) throw new Error(`Request failed: ${response.status}`);
            allAlerts = allAlerts.filter(a => a.id !== id);
            renderAlerts();
        } catch (error) {
            alert(`Couldn't delete alert: ${error.message}`);
        }
    }

    function setupTabs() {
        els.tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                els.tabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                activeTab = tab.dataset.tab;
                renderAlerts();
            });
        });
    }

    function setupGridDelete() {
        els.grid.addEventListener('click', (e) => {
            const btn = e.target.closest('.alert-delete-btn');
            if (btn) {
                deleteAlert(Number(btn.dataset.deleteId));
                return;
            }

            const card = e.target.closest('.alert-card');
            if (card) window.location.href = `alert-detail.html?id=${card.dataset.id}`;
        });
    }

    // --- Modal ---

    function resetModal() {
        selectedStock = null;
        els.searchInput.value = '';
        els.searchResults.innerHTML = '';
        els.selectedChip.hidden = true;
        document.getElementById('stock-search-step').hidden = false;
        els.newAlertForm.hidden = true;
        els.newAlertForm.reset();
        els.newAlertFeedback.hidden = true;
    }

    function openModal() {
        resetModal();
        els.modal.hidden = false;
    }

    function closeModal() {
        els.modal.hidden = true;
    }

    async function searchStocks(query) {
        try {
            const response = await fetch(`/api/stocks/search?${new URLSearchParams({ query })}`);
            if (!response.ok) throw new Error(`Request failed: ${response.status}`);
            const results = await response.json();
            els.searchResults.innerHTML = results.length === 0
                ? `<div class="stock-search-result-item">No matches.</div>`
                : results.map(s => `
                    <div class="stock-search-result-item" data-id="${s.id}" data-symbol="${s.symbol}" data-company="${s.companyName ?? ''}">
                        <strong>${s.symbol}</strong> — ${s.companyName ?? ''}
                    </div>`).join('');
        } catch (error) {
            els.searchResults.innerHTML = `<div class="stock-search-result-item" style="color:red">Error: ${error.message}</div>`;
        }
    }

    function selectStock(id, symbol, company) {
        selectedStock = { id: Number(id), symbol, company };
        els.selectedLabel.textContent = `${symbol} — ${company}`;
        els.selectedChip.hidden = false;
        document.getElementById('stock-search-step').hidden = true;
        els.newAlertForm.hidden = false;
    }

    function setupModal() {
        els.openModalBtn.addEventListener('click', openModal);
        els.closeModalBtn.addEventListener('click', closeModal);
        els.modal.addEventListener('click', (e) => { if (e.target === els.modal) closeModal(); });

        els.searchInput.addEventListener('input', () => {
            const query = els.searchInput.value.trim();
            clearTimeout(searchDebounce);
            if (query.length < 2) {
                els.searchResults.innerHTML = '';
                return;
            }
            searchDebounce = setTimeout(() => searchStocks(query), 2000);
        });

        els.searchResults.addEventListener('click', (e) => {
            const item = e.target.closest('.stock-search-result-item');
            if (item && item.dataset.id) {
                selectStock(item.dataset.id, item.dataset.symbol, item.dataset.company);
            }
        });

        els.changeStockBtn.addEventListener('click', () => {
            document.getElementById('stock-search-step').hidden = false;
            els.selectedChip.hidden = true;
            els.newAlertForm.hidden = true;
        });

        els.newAlertForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (!selectedStock) return;

            const condition = document.getElementById('new-alert-condition').value;
            const value = document.getElementById('new-alert-value').value;

            if (!value || Number(value) <= 0) {
                els.newAlertFeedback.textContent = 'Enter a valid price.';
                els.newAlertFeedback.className = 'alert-feedback error';
                els.newAlertFeedback.hidden = false;
                return;
            }

            const csrfToken = await getCsrfToken();
            try {
                const response = await fetch('/api/alerts', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        ...(csrfToken ? { 'X-CSRF-TOKEN': csrfToken } : {})
                    },
                    body: JSON.stringify({
                        stockId: selectedStock.id,
                        condition,
                        value: Number(value)
                    })
                });
                if (!response.ok) throw new Error(`Request failed: ${response.status}`);
                closeModal();
                await fetchAlerts();
            } catch (error) {
                els.newAlertFeedback.textContent = `Error: ${error.message}`;
                els.newAlertFeedback.className = 'alert-feedback error';
                els.newAlertFeedback.hidden = false;
            }
        });
    }

    setupTabs();
    setupGridDelete();
    setupModal();
    fetchAlerts();
})();