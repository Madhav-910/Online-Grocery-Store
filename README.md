# 🛒 UrbanFresh - Online Grocery Store




**UrbanFresh is a JavaFX-based grocery shopping application that allows users to register, log in, browse grocery products, add items to a cart, and place orders through a simple desktop interface.**

The project demonstrates the core workflow of an online grocery system using Java, JavaFX, JDBC, and MySQL.

---

## 📌 Project Overview

UrbanFresh provides a basic grocery shopping experience where users can explore available products, select quantities, manage their cart, and complete checkout. The application uses MySQL for user authentication and order management, while the user interface is built with JavaFX.

This project is mainly developed as an academic/demo application to understand desktop GUI development, database connectivity, and basic e-commerce system flow.

---

## ✨ Features

* User registration and login
* Grocery product browsing
* Product search functionality
* Add products to cart
* Quantity selection for each item
* Total price calculation
* Checkout with customer details
* Payment method selection
* MySQL-based user and order storage
* Clean JavaFX desktop interface

---

## 🧠 Core Idea

The application follows a simple grocery ordering workflow:

```text
User Login / Registration
        ↓
Product Browsing
        ↓
Search and Select Items
        ↓
Add Items to Cart
        ↓
Checkout
        ↓
Order Stored in Database
```

UrbanFresh is designed to show how a grocery store system can connect frontend interaction with backend database operations.

---

## 🛠 Tech Stack

| Technology        | Usage                      |
| ----------------- | -------------------------- |
| Java              | Main programming language  |
| JavaFX            | Desktop application UI     |
| MySQL             | Database management        |
| JDBC              | Java-to-MySQL connectivity |
| MySQL Connector/J | Database driver            |

---

## 🏗 Project Structure

```text
Online-Grocery-Store/
│
├── CartItem.java
├── DatabaseConnection.java
├── GroceryStoreApp.java
├── Product.java
└── ProductCard.java
```

### Main Files

| File                      | Description                                                                    |
| ------------------------- | ------------------------------------------------------------------------------ |
| `GroceryStoreApp.java`    | Main application file containing login, product page, cart, and checkout logic |
| `DatabaseConnection.java` | Handles MySQL connection, login validation, registration, and order storage    |
| `Product.java`            | Product model class                                                            |
| `ProductCard.java`        | JavaFX component used to display each grocery item                             |
| `CartItem.java`           | Cart item model with product quantity and price calculation                    |

---

## 🖼 Preview

> Add screenshots of the login page, product page, cart, and checkout page here after running the project.

```text
Login / Register Page
        ↓
Product Listing Page
        ↓
Shopping Cart
        ↓
Checkout Window
```

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Madhav-910/Online-Grocery-Store.git
cd Online-Grocery-Store
```

### 2. Install Requirements

Make sure the following are installed:

* JDK 17 or above
* JavaFX SDK
* MySQL Server
* MySQL Connector/J
* Any Java IDE such as IntelliJ IDEA, Eclipse, or VS Code

### 3. Configure Database Connection

In `GroceryStoreApp.java`, update the database credentials if needed:

```java
dbConnection = new DatabaseConnection(
    "jdbc:mysql://localhost:3306/urbanfresh",
    "root",
    "root"
);
```

Change `root` and `root` according to your MySQL username and password.

### 4. Run the Application

Open the project in your Java IDE and run:

```text
GroceryStoreApp.java
```

If JavaFX is not configured automatically, add VM options:

```bash
--module-path "path-to-javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
```

---

## 📌 Important Notes

* This is a JavaFX desktop application, not a full web-based grocery website.
* Product data is currently loaded inside the Java code.
* MySQL is used for user registration, login validation, and order storage.
* Passwords are stored as plain text in the current version, so this should not be used as a production system without security improvements.
* The project is best suited for academic learning and demonstration purposes.

---

## 🛣 Future Enhancements

* [ ] Add admin dashboard
* [ ] Store products directly in MySQL
* [ ] Add product categories
* [ ] Add stock management
* [ ] Add order history for users
* [ ] Add password hashing
* [ ] Improve UI design
* [ ] Add invoice generation
* [ ] Add payment gateway integration
* [ ] Add delivery tracking

---

## 📌 Project Status

UrbanFresh is currently a working JavaFX prototype with:

* user authentication
* grocery product display
* search functionality
* cart management
* checkout flow
* MySQL order storage

Future improvements can focus on making the system more secure, database-driven, and closer to a real grocery store application.

---

## 👨‍💻 Contributors

Developed as a JavaFX and MySQL-based grocery store management project.

GitHub Profiles:

* [Madhav-910](https://github.com/Madhav-910)
* [LIKHIL19](https://github.com/LIKHIL19)
