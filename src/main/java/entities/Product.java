package entities;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;


public class Product {

    private int productId;

    @NotEmpty(message = "Empty names are not allowed")
    private String name;

    @Min( value = 1, message = "rating must not be  less than 1")
    @Max( value = 10, message = "rating must not be greater than 10")

    private int rating;
    @NotNull(message = "category is required")
    private ProductCategory category;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime dateCreated;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateModified;



    public Product() {
        // Initialize any default values if needed
        this.dateCreated = LocalDateTime.now();
        this.dateModified = this.dateCreated;
    }



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
    @JsonIgnore
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonIgnore
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
        return "{" +
                "productId=" + productId +
                ", name=" + name +  // Remove single quotes
                ", rating=" + rating +
                ", category=" + category +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                '}';

    }



}
