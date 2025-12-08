package project;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imagePath;

    public Product(int id, String name, double price, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    // Getter for id
    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }
}
