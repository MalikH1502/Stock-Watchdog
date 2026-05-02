document.getElementById("Stock").addEventListener('submit', async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);  // event.target, not a separate form variable

    const payload = {
        symbol: formData.get("symbol"),
        companyName: formData.get("companyName"),  // fixed
        price: parseFloat(formData.get("price"))
    };

    console.log("Sending:", payload);  // should now show real values

    const response = await fetch("http://localhost:8080/api/stocks", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    const data = await response.json();
    console.log("Saved:", data);
});