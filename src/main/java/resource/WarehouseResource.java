package resource;

import entities.Product;
import entities.ProductCategory;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.WarehouseService;

import java.util.List;
import java.util.Optional;

@Path("/products")
public class WarehouseResource {
@Inject
     private  WarehouseService warehouseService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
     public List<Product> getAllProducts(){
        return warehouseService.getAllProducts();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewProduct ( Product product){
        warehouseService.addNewProduct(product);
        return Response.status(201).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
     public Response getProductById(@PathParam("id") int productId){
        Optional<Product> foundProduct =  warehouseService.getProductById(productId);
        return Response.status(200).entity(foundProduct).build();
    }

    @GET
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCategory(@PathParam("category") ProductCategory category){
        List<Product> products = warehouseService.getProductsByCategory(category);
        return Response.status(200).entity(products).build();

    }

}
