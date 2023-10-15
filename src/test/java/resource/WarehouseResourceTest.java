package resource;

import com.example.warehouse.HelloApplication;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import contextResolver.MyObjectMapperContextResolver;
import entities.Product;
import entities.ProductCategory;
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
        List<Product> mockProducts = List.of(new Product("telephone",5,ProductCategory.ELECTRONICS),new Product("laptop",7,ProductCategory.ELECTRONICS));

        // Use Mockito to mock the service's behavior
        Mockito.when(warehouseService.getAllProducts()).thenAnswer(invocation -> mockProducts);
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
        assertEquals(mockProducts, productsFromResponse);

    }



}
