const stockForm = document.getElementById("searchForm");
const resultsContainer = document.getElementById("results-container");

if (stockForm) {
    stockForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(event.target);

        const payload = {
            symbol: formData.get("symbol"),
        };

        resultsContainer.innerHTML = "<p>Loading...<p>";

        try{
        const response = await fetch("http://localhost:8080/api/stocks", {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        const data = await response.json();
        console.log("Got:", data);

        if(Array.isArray(data)){
            const htmlList = data.map(stock => `
                <div class="stock-row">
                    <span class="symbol">${stock.symbol}</span>
                    <span class="price">$${stock.price}</span>
                </div>
`).join('');

                resultsContainer.innerHTML = htmlList;
            }
        else{
        resultsContainer.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
        }}
        catch (error){
            resultsContainer.innerHTML = `<p style="color:red">Error: ${error.message}</p>`;
        }
    }
    );}



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