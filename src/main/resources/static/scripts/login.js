document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (response.ok) {
      const user = await response.json();

      // Check if the user's status is "PENDING"
      if (user.status === "PENDING") {
        document.getElementById("errorMessage").textContent = "Your account is pending approval. Please contact admin.";
        return; // Stop further execution
      }

      // Redirect based on the user's role
      if (user.role === "ADMIN") {
        window.location.href = "http://localhost:8080/dashboard/admin.html";
      } else if (user.role === "USER") {
        window.location.href = `/dashboard/user.html?id=${user.id}`;
      }
    } else {
      document.getElementById("errorMessage").textContent = "Invalid email or password.";
    }
  } catch (error) {
    console.error("Error during login:", error);
    document.getElementById("errorMessage").textContent = "An error occurred. Please try again.";
  }
});

