const searchInput = document.getElementById("stock-search");
const resultsContainer = document.getElementById("results-container");
const stockForm = document.getElementById("search-form");
const spinner = document.getElementById("search-spinner");

let debounceTimer = null;
let lastQuery = "";

function showSpinner() {
    if (spinner) spinner.classList.add("active");
    if (resultsContainer) resultsContainer.innerHTML = "";
}

function hideSpinner() {
    if (spinner) spinner.classList.remove("active");
}

async function performSearch(query) {
    const searchParams = new URLSearchParams({ query });
    const url = `http://localhost:8080/api/stocks/search?${searchParams}`;

    try {
        const response = await fetch(url, { method: "GET" });
        const data = await response.json();

        if (Array.isArray(data)) {
            const htmlList = data.map(stock => `
        <div class="stock-card" data-id="${stock.id}">
            <span class="symbol">${stock.symbol}</span>
            <span class="company-name">${stock.companyName ?? ''}</span>
            <div class="meta">
                <span class="badge badge-type">${stock.type ?? ''}</span>
<span class="badge">${getRegionFlag(stock.region)} ${stock.region ?? ''}</span>            </div>
        </div>
    `).join('');
            resultsContainer.innerHTML = htmlList;
        } else {
            resultsContainer.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
        }
    } catch (error) {
        resultsContainer.innerHTML = `<p style="color:red">Error: ${error.message}</p>`;
    } finally {
        hideSpinner();
    }
}


if (searchInput) {
    searchInput.addEventListener('input', () => {
        const query = searchInput.value.trim();

        clearTimeout(debounceTimer);

        if (query.length < 2) {
            hideSpinner();
            resultsContainer.innerHTML = "";
            lastQuery = "";
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

if (stockForm) {
    stockForm.addEventListener('submit', (event) => {
        event.preventDefault();
    });
}

if (resultsContainer) {
    resultsContainer.addEventListener('click', (event) => {
        const card = event.target.closest('.stock-card');
        if (card && card.dataset.id) {
            window.location.href = `stock-detail.html?id=${card.dataset.id}`;
        }
    });
}

const navButtons = document.querySelectorAll(".tab-container button[data-page]");

if (navButtons.length > 0) {
    const currentPath = globalThis.location.pathname.replace(/\/+$/, "") || "/";

    navButtons.forEach((button) => {
        const targetPath = new URL(button.dataset.page, globalThis.location.href).pathname.replace(/\/+$/, "") || "/";
        const row = button.closest("li");

        if (currentPath === targetPath && row) {
            row.classList.add("active");
        }

        button.addEventListener("click", () => {
            globalThis.location.href = button.dataset.page;
        });
    });
}