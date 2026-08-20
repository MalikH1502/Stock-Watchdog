const logoutButton = document.getElementById("logout-btn");
const logoutFeedback = document.getElementById("logout-feedback");

if (logoutButton) {
    logoutButton.addEventListener("click", async () => {
        logoutButton.disabled = true;
        if (logoutFeedback) {
            logoutFeedback.hidden = true;
        }

        const csrfToken = typeof getCsrfToken === "function" ? await getCsrfToken() : null;

        try {
            const response = await fetch("/api/logout", {
                method: "POST",
                headers: csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}
            });

            if (!response.ok) {
                throw new Error(`Request failed: ${response.status}`);
            }

            window.location.href = "/login.html";
        } catch (error) {
            logoutButton.disabled = false;
            if (logoutFeedback) {
                logoutFeedback.textContent = "Unable to log out. Please try again.";
                logoutFeedback.className = "alert-feedback error";
                logoutFeedback.hidden = false;
            }
        }
    });
}