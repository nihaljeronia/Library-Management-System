document.addEventListener("DOMContentLoaded", () => {
  fetchUsers(); // Fetch users on page load
  fetchTempBooks(); // Fetch temporary books on page load
  fetchSavedBooks(); // Fetch saved books on page load

  document.getElementById("logoutButton").addEventListener("click", logout);

  //Fetch Users
  async function fetchUsers() {
    try {
      const response = await fetch("/dashboard/admin/users");
      if (response.ok) {
        const users = await response.json();
        const usersTable = document.getElementById("usersTable").querySelector("tbody");
        usersTable.innerHTML = ""; // Clear the table

        users.forEach((user) => {
          let actions = "";

          if (user.role === "ADMIN" && user.status === "APPROVED") {
            actions = ""; // No actions for approved admin users
          } else if (user.status === "APPROVED") {
            actions = `
            <button onclick="snoozeUser(${user.id})">Snooze</button>
            <button onclick="deleteUser(${user.id})">Delete</button>
          `;
          } else if (user.status === "PENDING") {
            actions = `
            <button onclick="approveUser(${user.id})">Approve</button>
            <button onclick="rejectUser(${user.id})">Reject</button>
          `;
          }

          const row = `
          <tr data-user-id="${user.id}">
            <td>${user.id}</td>
            <td>${user.email}</td>
            <td>${user.role}</td>
            <td>${user.status}</td>
            <td>${actions}</td>
          </tr>
        `;
          usersTable.innerHTML += row;
        });
      } else {
        console.error("Failed to fetch users.");
      }
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  }

  // Toggle Add Book Form
  document.getElementById("toggleAddBookForm").addEventListener("click", () => {
    const form = document.getElementById("addBookForm");
    form.style.display = form.style.display === "none" ? "block" : "none";
  });

  // Handle Add Book Form Submission
  document.getElementById("addBookForm").addEventListener("submit", async function (e) {
    e.preventDefault(); // Prevent the default form submission behavior

    // Collect form data
    const book = {
      isbn: document.getElementById("isbn").value,
      title: document.getElementById("title").value,
      author: document.getElementById("author").value,
      genre: document.getElementById("genre").value,
      totalCopies: parseInt(document.getElementById("totalCopies").value),
    };

    try {
      // Send POST request to /books/temp
      const response = await fetch("/books/temp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(book),
      });

      if (response.ok) {
        alert("Book added successfully!");
        document.getElementById("addBookForm").reset(); // Clear the form
        fetchTempBooks(); // Refresh the temporary books preview
      } else {
        const errorData = await response.json();
        alert(`Failed to add book: ${errorData.message || "Unknown error"}`);
      }
    } catch (error) {
      console.error("Error adding book:", error);
      alert("An error occurred. Please try again.");
    }
  });

  document.getElementById("saveAllBooksButton").addEventListener("click", saveAllBooks);
});

// Approve a user
async function approveUser(id) {
  try {
    const response = await fetch(`http://localhost:8080/dashboard/admin/users/approve/${id}`, { method: "PUT" });
    if (response.ok) {
      alert("User approved successfully!");

      // Dynamically update the user's row in the table
      const userRow = document.querySelector(`tr[data-user-id="${id}"]`);
      fetchUsers();
    } else {
      alert("Failed to approve user.");
    }
  } catch (error) {
    console.error("Error approving user:", error);
  }
}

// Reject a user (remove from database)
async function rejectUser(id) {
  try {
    const response = await fetch(`http://localhost:8080/dashboard/admin/users/reject/${id}`, { method: "DELETE" });
    if (response.ok) {
      alert("User rejected and removed from the database.");
      fetchUsers(); // Refresh the table
    } else {
      alert("Failed to reject user.");
    }
  } catch (error) {
    console.error("Error rejecting user:", error);
  }
}

// Snooze a user (set status to PENDING)
async function snoozeUser(id) {
  try {
    const response = await fetch(`http://localhost:8080/dashboard/admin/users/snooze/${id}`, { method: "PUT" });
    if (response.ok) {
      alert("User snoozed successfully!");

      // Dynamically update the user's row in the table
      const userRow = document.querySelector(`tr[data-user-id="${id}"]`);
      fetchUsers();
    } else {
      alert("Failed to snooze user.");
    }
  } catch (error) {
    console.error("Error snoozing user:", error);
  }
}

// Delete a user
async function deleteUser(id) {
  try {
    const response = await fetch(`http://localhost:8080/dashboard/admin/users/delete/${id}`, { method: "DELETE" });
    if (response.ok) {
      alert("User deleted successfully!");
      fetchUsers(); // Refresh the table
    } else {
      alert("Failed to delete user.");
    }
  } catch (error) {
    console.error("Error deleting user:", error);
  }
}

// Fetch temporary books and toggle visibility of the preview section
async function fetchTempBooks() {
  try {
    const response = await fetch("/dashboard/admin/books/temp");
    const books = await response.json();

    const bookPreviewSection = document.getElementById("bookPreviewSection");
    const tempBooksTable = document.getElementById("tempBooksTable").querySelector("tbody");
    tempBooksTable.innerHTML = ""; // Clear the table

    if (Object.keys(books).length > 0) {
      bookPreviewSection.style.display = "block"; // Show the section if there are books
      Object.values(books).forEach((book) => {
        const row = `
          <tr>
            <td>${book.isbn}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.genre}</td>
            <td>${book.totalCopies}</td>
            <td>
              <button onclick="deleteTempBook('${book.isbn}')">🗑 Remove</button>
            </td>
          </tr>
        `;
        tempBooksTable.innerHTML += row;
      });
    } else {
      bookPreviewSection.style.display = "none"; // Hide the section if no books
    }
  } catch (error) {
    console.error("Error fetching temporary books:", error);
  }
}

// Delete a temporary book
async function deleteTempBook(isbn) {
  try {
    const response = await fetch(`/books/temp/${isbn}`, { method: "DELETE" });
    if (response.ok) {
      alert("Book removed from temporary storage successfully!");
      fetchTempBooks(); // Refresh the temporary books preview
    } else {
      alert("Failed to remove book. Please try again.");
    }
  } catch (error) {
    console.error("Error deleting book:", error);
    alert("An error occurred. Please try again.");
  }
}

// Save all temporary books to the database
async function saveAllBooks() {
  try {
    const response = await fetch("/books/save", { method: "POST" });
    if (response.ok) {
      alert("All temporary books saved to the database successfully!");
      fetchTempBooks(); // Clear the temporary books table
      fetchSavedBooks(); // Refresh the saved books table
    } else {
      alert("Failed to save books. Please try again.");
    }
  } catch (error) {
    console.error("Error saving books:", error);
    alert("An error occurred. Please try again.");
  }
}

// Fetch saved books and populate the table
async function fetchSavedBooks() {
  try {
    const response = await fetch("/books/all");
    const books = await response.json();

    const savedBooksTable = document.getElementById("savedBooksTable").querySelector("tbody");
    savedBooksTable.innerHTML = ""; // Clear the table

    books.forEach((book) => {
      const row = `
        <tr>
          <td>${book.isbn}</td>
          <td>${book.title}</td>
          <td>${book.author}</td>
          <td>${book.availableCopies}</td>
          <td>
            <button onclick="editBook('${book.id}')">Edit</button>
            <button onclick="deleteBook('${book.id}')">Delete</button>
          </td>
        </tr>
      `;
      savedBooksTable.innerHTML += row;
    });
  } catch (error) {
    console.error("Error fetching saved books:", error);
  }
}

// Edit a saved book
async function editBook(id) {
  const newISBN = prompt("Enter new ISBN");
  const newTitle = prompt("Enter new title:");
  const newAuthor = prompt("Enter new author:");
  const newGenre = prompt("Enter new genre:");
  const newTotalCopies = prompt("Enter new total copies:");

  if (!newISBN || !newTitle || !newAuthor || !newGenre || !newTotalCopies) {
    alert("All fields are required to edit the book.");
    return;
  }

  try {
    const response = await fetch(`/books/update/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        isbn: newISBN,
        title: newTitle,
        author: newAuthor,
        genre: newGenre,
        totalCopies: parseInt(newTotalCopies),
      }),
    });

    if (response.ok) {
      alert("Book updated successfully!");
      fetchSavedBooks(); // Refresh the saved books table
    } else {
      alert("Failed to update book. Please try again.");
    }
  } catch (error) {
    console.error("Error editing book:", error);
    alert("An error occurred. Please try again.");
  }
}

// Delete a saved book
async function deleteBook(id) {
  try {
    if (!confirm("Are you sure you want to delete this book?")) {
      return;
    }
    const response = await fetch(`/books/delete/${id}`, { method: "DELETE" });

    if (response.ok) {
      fetchSavedBooks(); // Refresh the saved books table
    } else {
      console.error("Failed to delete book.");
    }
  } catch (error) {
    console.error("Error deleting book:", error);
  }
}

// Logout
function logout() {
  window.location.href = "/login.html";
}