package resource;

import entities.Product;
import entities.ProductCategory;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.WarehouseService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Path("/products")
public class WarehouseResource {
@Inject
     private  WarehouseService warehouseService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
     public Response getAllProducts(){
        try{
            List<Product> products =  warehouseService.getAllProducts();
            return Response.status(200).entity(products).build();

        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured while retriving products").build();
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewProduct ( @Valid Product product){
        try{
            warehouseService.addNewProduct(product);
            return Response.status(Response.Status.CREATED).entity("product successfully added").build();

        }catch(ValidationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        }
        catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while adding the products").build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
     public Response getProductById(@PathParam("id") int productId){
        Optional<Product> foundProduct =  warehouseService.getProductById(productId);
        if(foundProduct.isPresent()){
            return Response.status(Response.Status.ACCEPTED).entity(foundProduct).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("product not found with ID: " + productId).build();
        }

    }

    @GET
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCategory(@PathParam("category") ProductCategory category){
        try{
            List<Product> products = warehouseService.getProductsByCategory(category);
            if(products!= null){
                return Response.status(200).entity(products).build();
            }else{
                return Response.status(Response.Status.NOT_FOUND).entity("no products found for the given category: " + category).build();
            }

        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured while retriving products by category").build();

        }



    }

}
