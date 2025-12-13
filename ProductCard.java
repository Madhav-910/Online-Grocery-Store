package project;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ProductCard extends HBox {
    private Product product;
    private GroceryStoreApp app;

    public ProductCard(Product product, GroceryStoreApp app) {
        this.product = product;
        this.app = app;

        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #e9ecef; -fx-border-color: #007BFF; -fx-border-radius: 5; -fx-background-radius: 5;");

        ImageView productImage = new ImageView(new Image(product.getImagePath()));
        productImage.setFitWidth(100);
        productImage.setFitHeight(100);
        
        VBox detailsBox = new VBox(5);
        Label productName = new Label(product.getName());
        productName.setFont(new Font("Arial", 16));
        productName.setTextFill(Color.web("#007BFF"));
        
        Label productPrice = new Label("Price: ₹" + product.getPrice());
        productPrice.setFont(new Font("Arial", 14));

        ComboBox<Integer> quantityDropdown = new ComboBox<>();
        for (int i = 1; i <= 10; i++) {
            quantityDropdown.getItems().add(i);
        }
        quantityDropdown.setValue(1); // Default quantity

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> {
            int quantity = quantityDropdown.getValue();
            app.addToCart(product, quantity);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(quantity + " " + product.getName() + "(s) added to cart.");
            alert.setTitle("Added to Cart");
            alert.showAndWait();
        });

        detailsBox.getChildren().addAll(productName, productPrice, quantityDropdown, addToCartButton);
        getChildren().addAll(productImage, detailsBox);
    }
}
