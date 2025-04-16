document.getElementById("registerForm").addEventListener("submit", async function (e) {
  e.preventDefault(); // Prevent default form submission

  const newUser = {
    name: document.getElementById("name").value,
    email: document.getElementById("email").value,
    password: document.getElementById("password").value,
    role: document.getElementById("role").value
  };

  console.log("Registering user:", newUser); // Debugging

  try {
    const response = await fetch("http://localhost:8080/dashboard/admin/users/register", { 
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newUser),
    });

    console.log("Response status:", response.status); // Debugging

    if (response.ok) {
      document.getElementById("message").textContent = "Registration successful! You will be approved by an admin.";
      document.getElementById("registerForm").reset();
    } else {
      const error = await response.json();
      console.error("Registration failed:", error); // Debugging
      alert(error.message || "Registration failed. Please try again.");
    }
  } catch (error) {
    console.error("Error registering user:", error);
    alert("An error occurred. Please try again.");
  }
});