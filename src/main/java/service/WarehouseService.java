package service;

import entities.Product;
import entities.ProductCategory;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


@ApplicationScoped
public class WarehouseService {

    private final List<Product> products = new ArrayList<>();
   private final Lock lock = new ReentrantLock();
    //private int maxProductId = 0;


    public void addNewProduct(Product product) {
        lock.lock();
        try {
            int productId = generateUniqueId();
            product.setProductId(productId);
            products.add(product);
            System.out.println("Product successfully added");

        }finally {
            lock.unlock(); // release the lock
        }


    }

    private int generateUniqueId(){
        return products.size() + 1;
    }

    public void editProduct(int productId, String name, int rating, ProductCategory category) {
        lock.lock();

        try{
            Optional<Product> productToUpdate = getProductById(productId);

            if (productToUpdate.isPresent()) {
                Product product = productToUpdate.get();
                if (name != null && !name.isEmpty()) {
                    product.setName(name);
                }
                if (category != null) {
                    product.setProductCategory(category);
                }
                if (rating >= 0 && rating <= 10) {
                    product.setRating(rating);
                }

                product.updateDateModified();
                System.out.println("Product edited successfully");

            } else {
                throw new IllegalArgumentException("Product of ID " + productId + " not found");
            }


        }finally {
            lock.unlock();
        }

    }

    // get all products
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // get product by id
    public Optional<Product> getProductById(int productId) {
        return getAllProducts().stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst();
    }

    // get products by category
    public List<Product> getProductsByCategory(ProductCategory category){
        return  getAllProducts().stream()
                .filter(pdt->pdt.getCategory()== category)
                .sorted((p1,p2)->p1.getName().compareToIgnoreCase(p2.getName()))
                .collect(Collectors.toList());
    }

    // get all products created after a given date. New products then last

    public List<Product> getProductsCreatedAfterDateSortedByNewest(LocalDateTime date){
        if(date==null){
            throw new IllegalArgumentException("Date Cannot be null");
        }
        return getAllProducts().stream()
                .filter(product -> product.getDateCreated().isAfter(date))
                .sorted(Comparator.comparing(Product::getDateCreated).reversed())
                .collect(Collectors.toList());


    }


    // get all products modified after creation
    public List<Product> getProductsModifiedAfterCreation() {
        return  getAllProducts().stream()
                .filter(product -> product.getDateModified().isAfter(product.getDateCreated()))
                .collect(Collectors.toList());


    }

    // get all categories that has minimum one product attached
    public Set<ProductCategory> getCategoriesWithProducts() {
        return getAllProducts().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull) // Filter out null categories (if any)
                .collect(Collectors.toSet());
    }

    // get all products found in a category
    public Map<ProductCategory, Long> getProductCountByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
    }

    // get product count by starting letter
    public Map<Character, Long> getProductCountByStartingLetter() {
        return getAllProducts().stream()
                .map(Product::getName)
                .collect(Collectors.groupingBy(name -> name.charAt(0), Collectors.counting()));

    }

    // get products with max rating

    public List<Product> getProductsRatingCreatedThisMonthSortedByDate() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = LocalDateTime.of(currentDate.getYear(), currentDate.getMonth(), 1,0,0);
        LocalDateTime endOfMonth = LocalDateTime.of(currentDate.getYear(), currentDate.getMonth(), currentDate.getMonth().maxLength(), 23, 59);

        return getAllProducts().stream()
                .filter(product -> product.getRating() == 10)
                .filter(product -> {
                    LocalDateTime creationDateTime = product.getDateCreated();
                    return creationDateTime.isAfter(firstDayOfMonth) && creationDateTime.isBefore(endOfMonth);
                })
                .sorted(Comparator.comparing(Product::getDateModified).reversed())
                .collect(Collectors.toList());
    }



}

