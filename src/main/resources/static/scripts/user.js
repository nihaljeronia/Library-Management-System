async function borrowBook(isbn, userId) {
  try {
    const payload = { userId: userId, isbn: isbn }; // Send isbn instead of bookId
    console.log("Payload being sent:", payload); // Debugging

    const response = await fetch("/borrow", {
      method: "POST",
      headers: {
        "Content-Type": "application/json", // Ensure the Content-Type is set to application/json
      },
      body: JSON.stringify(payload), // Send userId and isbn as JSON
    });

    if (response.ok) {
      alert("Book borrowed successfully!");
      fetchSavedBooks(); // Refresh the saved books table
      fetchBorrowedBooks(userId); // Refresh the borrowed books table
    } else {
      const error = await response.text();
      console.error("Failed to borrow book:", error);
      alert("Failed to borrow book: " + error);
    }
  } catch (error) {
    console.error("Error borrowing book:", error);
  }
}

// Return a book (Global Scope)
async function returnBook(id) {
  try {
    const response = await fetch(`/borrow/return/${id}`, { method: "PUT" });
    if (response.ok) {
      alert("Book returned successfully!");
      fetchBorrowedBooks(userId);
      fetchBorrowingHistory(userId);
    } else {
      alert("Failed to return book.");
    }
  } catch (error) {
    console.error("Error returning book:", error);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const userId = new URLSearchParams(window.location.search).get("id");
  const welcomeMessage = document.getElementById("welcomeMessage");
  welcomeMessage.textContent = `Welcome, User ${userId}`;

  // Logout button
  document.getElementById("logoutButton").addEventListener("click", () => {
    window.location.href = "http://localhost:8080/login.html";
  });

  // Fetch and display all sections
  fetchSavedBooks();
  fetchBorrowedBooks(userId);
  fetchBorrowingHistory(userId);

  // Fetch saved books
  async function fetchSavedBooks() {
    try {
      const response = await fetch("http://localhost:8080/books/all");
      const books = await response.json();

      const savedBooksTable = document.getElementById("savedBooksTable").querySelector("tbody");
      savedBooksTable.innerHTML = ""; // Clear the table

      books.forEach((book) => {
        const borrowButton = book.availableCopies > 0
          ? `<button onclick="borrowBook(${book.isbn}, ${userId})">Borrow</button>`
          : `<span style="color: gray;">Not Available</span>`;

        const row = `
          <tr>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.genre}</td>
            <td>${book.availableCopies}</td>
            <td>${borrowButton}</td>
          </tr>
        `;
        savedBooksTable.innerHTML += row;
      });
    } catch (error) {
      console.error("Error fetching saved books:", error);
    }
  }

  // Cache for book data to avoid repeated API calls
let bookCache = null;

// Helper function to fetch all books and cache them
async function getBooks() {
if (bookCache) return bookCache;
try {
const response = await fetch("http://localhost:8080/books/all");
if (!response.ok) throw new Error("Failed to fetch books");
bookCache = await response.json();
console.log("Books fetched:", bookCache); // Debug
return bookCache;
} catch (error) {
console.error("Error fetching books:", error);
return [];
}
}

// Helper function to get title by ISBN
async function getBookTitleByIsbn(isbn) {
if (!isbn) {
console.log("No ISBN provided"); // Debug
return "N/A";
}
const books = await getBooks();
const book = books.find(book => book.isbn === isbn);
const title = book ? book.title : "N/A";
return title;
}

// Update fetchBorrowedBooks
async function fetchBorrowedBooks(userId) {
try {
const response = await fetch(`/borrow/user/${userId}`);
if (!response.ok) throw new Error("Failed to fetch borrowed books");

const books = await response.json();
console.log("Borrowed books:", books); // Debug
const tableBody = document.getElementById("borrowedBooksTable").querySelector("tbody");
tableBody.innerHTML = "";

for (const book of books.filter(b => !b.returned)) {
  const title = await getBookTitleByIsbn(book.isbn);
  const row = `
    <tr>
      <td>${title}</td>
      <td>${new Date(book.borrowDate).toLocaleString()}</td>
      <td>${book.returned ? "Returned" : "Not Returned"}</td>
      <td><button onclick="returnBook(${book.id})">Return</button></td>
    </tr>
  `;
  tableBody.innerHTML += row;
}
} catch (error) {
console.error("Error fetching borrowed books:", error);
}
}

// Update fetchBorrowingHistory
async function fetchBorrowingHistory(userId) {
try {
const response = await fetch(`/borrow/user/${userId}`);
if (!response.ok) throw new Error("Failed to fetch history");

const history = await response.json();
console.log("Borrowing history:", history); // Debug
const tableBody = document.getElementById("borrowingHistoryTable").querySelector("tbody");
tableBody.innerHTML = "";

for (const record of history) {
  const title = await getBookTitleByIsbn(record.isbn);
  const row = `
    <tr>
      <td>${title}</td>
      <td>${new Date(record.borrowDate).toLocaleString()}</td>
      <td>${record.returnDate ? new Date(record.returnDate).toLocaleString() : "Not Returned"}</td>
      <td>${record.returned ? "Returned" : "Not Returned"}</td>
    </tr>
  `;
  tableBody.innerHTML += row;
}
} catch (error) {
console.error("Error fetching borrowing history:", error);
}
}
});
