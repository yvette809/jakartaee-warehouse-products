package resource;

import com.example.warehouse.HelloApplication;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import contextResolver.MyObjectMapperContextResolver;
import entities.Product;
import entities.ProductCategory;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import service.MyException;
import service.MyExceptionMapper;
import service.WarehouseService;


import java.text.SimpleDateFormat;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


@ExtendWith(MockitoExtension.class)

public class WarehouseResourceTest {

    @Mock
    WarehouseService warehouseService;
    Dispatcher dispatcher;
    @BeforeEach
    public void setUp(){

        dispatcher = MockDispatcherFactory.createDispatcher();
        var warehouseResource = new WarehouseResource(warehouseService);
        dispatcher.getRegistry().addSingletonResource(warehouseResource);
        // create custom exception mapper
        ExceptionMapper<MyException> mapper = new MyExceptionMapper();

        // register custom exception mapper
        dispatcher.getProviderFactory().registerProviderInstance(mapper);
        dispatcher.getProviderFactory().registerProvider(MyObjectMapperContextResolver.class);
    }


    @Test
    public void getProductsReturnsStatus200AndProducts() throws Exception {


        // Create a mock list of products
        List<Product> mockProducts = List.of(new Product(1,"iphone",6,ProductCategory.ELECTRONICS),new Product(2,"shirt",8,ProductCategory.CLOTHING));

        // Use Mockito to mock the service's behavior
       // Mockito.when(warehouseService.getAllProducts()).thenAnswer(invocation -> mockProducts);
        Mockito.when(warehouseService.getAllProducts()).thenReturn(mockProducts);

        MockHttpRequest request = MockHttpRequest.get("/products");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        // Deserialize the response content into a list of products
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println("Response Content: " + response.getContentAsString());

        List<Product> productsFromResponse = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
        System.out.println("Products from Response: " + productsFromResponse);

        // Assert the response status code
        assertEquals(200, response.getStatus());

        // Assert the deserialized products against the expected products
        System.out.println("mockproducts: " + mockProducts);
        System.out.println("productsFromResponse: " + productsFromResponse);
        assertEquals(mockProducts, productsFromResponse);
        // Verify that the warehouseService.getAllProducts() method was called exactly once
        Mockito.verify(warehouseService, Mockito.times(1)).getAllProducts();

    }

    @Test
    public void getProductByIdReturnsStatus200AndProduct() throws Exception {
        // Create a mock product
        Product mockProduct = new Product(1, "iphone", 6, ProductCategory.ELECTRONICS);

        // Configure the behavior of the warehouseService mock for getProductById
        Mockito.when(warehouseService.getProductById(1)).thenReturn(Optional.of(mockProduct));

        // Create a mock HTTP request to get the product by its ID
        MockHttpRequest request = MockHttpRequest.get("/products/1");
        MockHttpResponse response = new MockHttpResponse();

        // Perform the request and capture the response
        dispatcher.invoke(request, response);

        //Deserialize the response content into a list of products
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Assert the response status code
        assertEquals(200, response.getStatus());


        // Deserialize the response content into a product
        Product productFromResponse = objectMapper.readValue(response.getContentAsString(), new TypeReference<Product>() {});

        System.out.println("productResponse: " + productFromResponse);

        // Assert the deserialized product against the expected product
        assertEquals(mockProduct, productFromResponse);

        // Verify that the warehouseService.getProductById(1) method was called exactly once
        Mockito.verify(warehouseService, Mockito.times(1)).getProductById(1);
    }

    @Test
    public void addProductReturnsStatus201AndNewProduct() throws Exception {
        // Create a mock product to be added
        Product mockProduct = new Product(1, "iphone", 6, ProductCategory.ELECTRONICS);

        // Configure the behavior of the warehouseService mock to handle product addition
        // Assuming the addNewProduct method returns void
        Mockito.doNothing().when(warehouseService).addNewProduct(Mockito.any(Product.class));

        // Create a mock HTTP request to add a new product
        MockHttpRequest request = MockHttpRequest.post("/products");
        String productJson = new ObjectMapper().writeValueAsString(mockProduct); // Serialize the mockProduct to JSON
        request.content(productJson.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        // Perform the request and capture the response
        dispatcher.invoke(request, response);

        // Assert the response status code
        assertEquals(201, response.getStatus());

        // Deserialize the response content into a product
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Product productFromResponse = objectMapper.readValue(response.getContentAsString(), new TypeReference<Product>() {});

        // Assert the deserialized product against the expected product
        assertEquals(mockProduct, productFromResponse);

        // Verify that the warehouseService.addProduct() method was called exactly once with the expected product
        Mockito.verify(warehouseService, Mockito.times(1)).addNewProduct(mockProduct);
    }

    @Test
    public void getProductsByCategoryReturnsStatus200AndProducts() throws Exception {
        // Create a mock list of products for a specific category
        List<Product> mockProducts = List.of(
                new Product(1, "iphone", 6, ProductCategory.ELECTRONICS),
                new Product(2, "laptop", 10, ProductCategory.ELECTRONICS)
        );

        // Configure the behavior of the warehouseService mock to return products for a specific category
        Mockito.when(warehouseService.getProductsByCategory(ProductCategory.ELECTRONICS)).thenReturn(mockProducts);

        // Create a mock HTTP request to get products by a specific category
        MockHttpRequest request = MockHttpRequest.get("/products/category/ELECTRONICS");
        MockHttpResponse response = new MockHttpResponse();

        // Perform the request and capture the response
        dispatcher.invoke(request, response);

        // Assert the response status code
        assertEquals(200, response.getStatus());

        // Deserialize the response content into a list of products
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Product> productsFromResponse = objectMapper.readValue(response.getContentAsString(), new com.fasterxml.jackson.core.type.TypeReference<List<Product>>() {});

        // Assert the deserialized products against the expected products
        assertEquals(mockProducts, productsFromResponse);

        // Verify that the warehouseService.getProductsByCategory() method was called exactly once with the expected category
        Mockito.verify(warehouseService, Mockito.times(1)).getProductsByCategory(ProductCategory.ELECTRONICS);
    }

}




