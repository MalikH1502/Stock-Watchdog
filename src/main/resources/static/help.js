(function () {
    const ARTICLES = [
        { category: "Getting Started", title: "How do I find a stock?",
          body: "Use the search bar on the Dashboard. Type at least 2 characters and pause. Results load after a short delay to avoid wasting API calls. Click a card to open its detail page." },
        { category: "Getting Started", title: "What does 'Total Tracked' mean?",
          body: "It counts stocks you have opened on a detail page at least once, not every stock that appeared in search results." },
        { category: "Alerts", title: "How do I set a price alert?",
          body: "Open a stock's detail page and use Set Alert, or click Add New Alert on the Alerts page. Choose Above or Below, enter a target price, and save." },
        { category: "Alerts", title: "When does an alert trigger?",
          body: "Alerts are checked in your browser when you load the Dashboard, Alerts page, or a stock detail page. If the latest price meets your condition, the alert is marked Triggered. Alerts are not checked while the site is closed." },
        { category: "Alerts", title: "Why didn't I get a desktop notification?",
          body: "Notifications must be enabled in Settings and allowed in your browser's site settings. They only appear while a StockWatch tab is open." },
        { category: "Prices & Data", title: "How fresh are the prices?",
          body: "Prices are cached and refreshed from Alpha Vantage when you open a stock and its price is more than 5 minutes old. The coloured timer on each card shows the age: green under 5 minutes, amber under 30, red after that." },
        { category: "Prices & Data", title: "Why is a price missing or not updating?",
          body: "The free data tier allows 25 requests per day across all searches and quotes. When the limit is hit, StockWatch shows the last saved price instead." },
        { category: "Account", title: "How do I log out?",
          body: "Go to Settings and click Log Out. Sessions also expire after 30 minutes of inactivity." }
    ];

    const els = {
        search: document.getElementById('help-search'),
        chips: document.getElementById('help-chips'),
        results: document.getElementById('help-results'),
        empty: document.getElementById('help-empty')
    };
    const categories = ['All', ...new Set(ARTICLES.map(a => a.category))];
    let activeCategory = 'All';

    function renderChips() {
        els.chips.innerHTML = categories.map(c =>
            `<button type="button" class="chip ${c === activeCategory ? 'active' : ''}" data-cat="${c}">${c}</button>`
        ).join('');
    }

    function render() {
        const q = els.search.value.trim().toLowerCase();
        const matches = ARTICLES.filter(a =>
            (activeCategory === 'All' || a.category === activeCategory) &&
            (!q || a.title.toLowerCase().includes(q) || a.body.toLowerCase().includes(q)));
        els.empty.hidden = matches.length > 0;
        els.results.innerHTML = matches.map(a => `
            <details class="faq-item">
                <summary><span>${a.title}</span><span class="faq-cat">${a.category}</span></summary>
                <p>${a.body}</p>
            </details>`).join('');
    }

    els.search.addEventListener('input', render);
    els.search.addEventListener('keydown', e => { if (e.key === 'Enter') e.preventDefault(); });
    els.chips.addEventListener('click', e => {
        const btn = e.target.closest('.chip');
        if (!btn) return;
        activeCategory = btn.dataset.cat;
        renderChips();
        render();
    });

    renderChips();
    render();
})();