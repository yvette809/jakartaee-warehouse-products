package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Product {

    private int productId;
    private String name;
    private int rating;
    private ProductCategory category;
    private final LocalDateTime dateCreated;
    private LocalDateTime dateModified;



    public Product(String name, int rating, ProductCategory category) {
        setProductId(productId);
        this.name = name;
        this.rating = rating;
        this.category = category;
        this.dateCreated = LocalDateTime.now();
        this.dateModified = this.dateCreated;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }


    public int getRating() {
        return rating;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void updateDateModified() {
        this.dateModified = LocalDateTime.now();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductId(int productId) {
        this.productId = productId;

    }

    public void setRating(int rating) {
        if (rating >= 0 && rating <= 10) {
            this.rating = rating;
        }


    }

    public void setProductCategory(ProductCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", category=" + category +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                '}';
    }
}
