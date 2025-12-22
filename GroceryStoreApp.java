package project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroceryStoreApp extends Application {
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> displayedProducts = new ArrayList<>();
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private double totalPrice = 0.0;
    private String loggedInUsername;  // To hold the logged-in user's name
    private DatabaseConnection dbConnection;
    private Map<Integer, Integer> productQuantities = new HashMap<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        dbConnection = new DatabaseConnection("jdbc:mysql://localhost:3306/urbanfresh", "root", "root");
        if (!dbConnection.isConnected()) {
            showAlert("Cannot connect to the database. Please check your settings.", "Database Connection Error");
            return;
        }
        loadProducts();
        showLoginOrRegisterPage(primaryStage);
    }

    private void showLoginOrRegisterPage(Stage primaryStage) {
        primaryStage.setTitle("Login or Register - Urban Fresh");

        GridPane authPane = new GridPane();
        authPane.setHgap(10);
        authPane.setVgap(10);
        authPane.setPadding(new Insets(20));
        authPane.setStyle("-fx-background-color: #f7f7f7;");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button toggleButton = new Button("Switch to Register");

        toggleButton.setOnAction(e -> {
            if (registerButton.isVisible()) {
                registerButton.setVisible(false);
                loginButton.setVisible(true);
                toggleButton.setText("Switch to Register");
            } else {
                registerButton.setVisible(true);
                loginButton.setVisible(false);
                toggleButton.setText("Switch to Login");
            }
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                showAlert("Username and password cannot be empty.", "Login Failed");
            } else if (dbConnection.validateLogin(username, password)) {
                loggedInUsername = username;
                primaryStage.close();
                showProductPage();
            } else {
                showAlert("Invalid username or password.", "Login Failed");
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                showAlert("Username and password cannot be empty.", "Registration Failed");
            } else if (password.length() < 6) {
                showAlert("Password must be at least 6 characters long.", "Registration Failed");
            } else if (dbConnection.registerUser(username, password)) {
                showAlert("Registration successful! You can now log in.", "Registration Successful");
                usernameField.clear();
                passwordField.clear();
            } else {
                showAlert("Username already exists or other error occurred.", "Registration Failed");
            }
        });

        authPane.add(new Label("Username:"), 0, 0);
        authPane.add(usernameField, 1, 0);
        authPane.add(new Label("Password:"), 0, 1);
        authPane.add(passwordField, 1, 1);
        authPane.add(loginButton, 0, 2);
        authPane.add(registerButton, 1, 2);
        authPane.add(toggleButton, 0, 3);

        registerButton.setVisible(false); // Initially hide the register button

        Scene scene = new Scene(authPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showProductPage() {
        Stage productStage = new Stage();
        productStage.setTitle("Urban Fresh - Online Grocery Store");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ffffff;");

        Label titleLabel = new Label("Welcome to Urban Fresh!");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.web("#007BFF")); // Set title color

        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Button clearSearchButton = new Button("Clear Search");

        searchButton.setOnAction(e -> filterProductList(searchField.getText().toLowerCase()));
        clearSearchButton.setOnAction(e -> {
            searchField.clear();
            updateProductGrid(allProducts); // Show all products
        });

        HBox searchBox = new HBox(10, new Label("Search Products:"), searchField, searchButton, clearSearchButton);
        root.getChildren().addAll(titleLabel, searchBox);

        // Product Grid with ScrollPane
        VBox productContainer = new VBox(10);
        productContainer.getChildren().add(new Label("Available Products:"));
        ScrollPane productScrollPane = new ScrollPane();
        productScrollPane.setContent(createProductGrid());
        productScrollPane.setFitToWidth(true);
        productScrollPane.setPrefHeight(400); // Set preferred height for scroll pane

        // Cart Button
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(e -> showCartWindow());

        productContainer.getChildren().addAll(productScrollPane, viewCartButton);
        root.getChildren().add(productContainer);

        Scene scene = new Scene(root, 800, 600);
        productStage.setScene(scene);
        productStage.show();
    }

    private GridPane productGrid;  // Declare this at the top of your class

    private GridPane createProductGrid() {
        productGrid = new GridPane(); // Initialize productGrid
        productGrid.setHgap(10);
        productGrid.setVgap(10);
        productGrid.setStyle("-fx-background-color: #f0f0f0;"); // Light background for the grid
        updateProductGrid(allProducts); // Populate the grid with products
        return productGrid;
    }

    private void updateProductGrid(List<Product> products) {
        productGrid.getChildren().clear(); // Clear the existing grid
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            ProductCard productCard = new ProductCard(product, this);
            productGrid.add(productCard, i % 3, i / 3);
        }
    }

    private void filterProductList(String searchTerm) {
        displayedProducts.clear();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(searchTerm)) {
                displayedProducts.add(product);
            }
        }
        updateProductGrid(displayedProducts); // Update the grid with filtered products
    }

    private void loadProducts() {
        // Adding more diverse products
        allProducts.add(new Product(1, "Apple", 30.00, "https://upload.wikimedia.org/wikipedia/commons/1/15/Red_Apple.jpg"));
        allProducts.add(new Product(2, "Banana", 10.00, "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg"));
        allProducts.add(new Product(3, "Orange", 25.00, "https://upload.wikimedia.org/wikipedia/commons/b/bf/Orange_Fruit_Close-up.jpg"));
        allProducts.add(new Product(4, "Grapes", 50.00, "https://upload.wikimedia.org/wikipedia/commons/b/bb/Table_grapes_on_white.jpg"));
        allProducts.add(new Product(5, "Dragonfruit", 40.00, "https://upload.wikimedia.org/wikipedia/commons/4/43/Pitaya_cross_section_ed2.jpg"));
        allProducts.add(new Product(6, "Kiwi", 60.00, "https://upload.wikimedia.org/wikipedia/commons/d/d3/Kiwi_aka.jpg"));
        allProducts.add(new Product(7, "Tomato", 20.00, "https://upload.wikimedia.org/wikipedia/commons/8/88/Bright_red_tomato_and_cross_section02.jpg"));
        allProducts.add(new Product(8, "Potato", 15.00, "https://upload.wikimedia.org/wikipedia/commons/b/be/Potato_var._Linda_HC1.JPG"));
        allProducts.add(new Product(9, "Onion", 12.00, "https://upload.wikimedia.org/wikipedia/commons/2/25/Onion_on_White.JPG"));
        allProducts.add(new Product(10, "Carrot", 18.00, "https://upload.wikimedia.org/wikipedia/commons/d/dc/Carrot-fb.jpg"));
        allProducts.add(new Product(11, "Cabbage", 30.00, "https://upload.wikimedia.org/wikipedia/commons/7/7f/Chou_cabus_blanc_01.jpg"));
        allProducts.add(new Product(12, "Eggplant", 45.00, "https://upload.wikimedia.org/wikipedia/commons/f/fb/Aubergine.jpg"));
        allProducts.add(new Product(13, "Bell Pepper", 35.00, "https://upload.wikimedia.org/wikipedia/commons/5/5c/Red_bell_pepper.jpg"));
        allProducts.add(new Product(14, "Pineapple", 80.00, "https://upload.wikimedia.org/wikipedia/commons/7/7a/Ananas~May_2008-1.jpg"));
        allProducts.add(new Product(15, "Mango", 75.00, "https://upload.wikimedia.org/wikipedia/commons/e/ee/Mango_and_cross_section_edit.jpg"));
    }

    private void showCartWindow() {
    	 Stage cartStage = new Stage();
    	    cartStage.setTitle("Shopping Cart - Urban Fresh");

    	    VBox cartLayout = new VBox(10);
    	    cartLayout.setPadding(new Insets(20));
    	    cartLayout.setStyle("-fx-background-color: #ffffff;");

    	    Label cartLabel = new Label("Your Shopping Cart");
    	    cartLabel.setFont(new Font("Arial", 24));
    	    cartLabel.setTextFill(Color.web("#007BFF"));

    	    ListView<CartItem> cartListView = new ListView<>();
    	    cartListView.setItems(cartItems);

        Button clearCartButton = new Button("Clear Cart");
        clearCartButton.setOnAction(e -> {
            cartItems.clear();
            totalPrice = 0.0;
            cartListView.getItems().clear();
            showAlert("Your cart has been cleared.", "Cart Cleared");
        });

        Button checkoutButton = new Button("Proceed to Checkout");
        checkoutButton.setOnAction(e -> showCheckoutWindow());

        HBox totalLayout = new HBox(10, new Label("Total Price: ₹" + totalPrice), checkoutButton);
        totalLayout.setAlignment(Pos.CENTER);

        cartLayout.getChildren().addAll(cartLabel, cartListView, clearCartButton, totalLayout);

        Scene scene = new Scene(cartLayout, 400, 300);
        cartStage.setScene(scene);
        cartStage.show();
    }

    private void showCheckoutWindow() {
        Stage checkoutStage = new Stage();
        checkoutStage.setTitle("Checkout - Urban Fresh");

        VBox checkoutLayout = new VBox(10);
        checkoutLayout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneField = new TextField();
        ComboBox<String> paymentMethodDropdown = new ComboBox<>();
        paymentMethodDropdown.getItems().addAll("Credit Card", "Debit Card", "UPI");

        Button confirmButton = new Button("Confirm Order");
        confirmButton.setOnAction(e -> {
            if (cartItems.isEmpty()) {
                showAlert("Your cart is empty.", "Checkout Failed");
            } else if (phoneField.getText().trim().isEmpty()) {
                showAlert("Phone number cannot be empty.", "Checkout Failed");
            } else if (paymentMethodDropdown.getValue() == null) {
                showAlert("Please select a payment method.", "Checkout Failed");
            } else {
                boolean orderStored = dbConnection.storeOrder(loggedInUsername, totalPrice, paymentMethodDropdown.getValue(), new ArrayList<>(cartItems));
                if (orderStored) {
                    showAlert("Order placed successfully!", "Checkout Successful");
                    cartItems.clear();
                    totalPrice = 0.0;
                    checkoutStage.close();
                } else {
                    showAlert("Failed to place order.", "Checkout Failed");
                }
            }
        });

        checkoutLayout.getChildren().addAll(new Label("Name:"), nameField,
                new Label("Address:"), addressField,
                new Label("Phone:"), phoneField,
                new Label("Payment Method:"), paymentMethodDropdown,
                confirmButton);

        Scene scene = new Scene(checkoutLayout, 400, 300);
        checkoutStage.setScene(scene);
        checkoutStage.show();
    }

    public void addToCart(Product product, int quantity) {
        CartItem cartItem = new CartItem(product, quantity);
        cartItems.add(cartItem);
        totalPrice += product.getPrice() * quantity;
    }

    private void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}