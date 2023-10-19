package resource;

import entities.Product;
import entities.ProductCategory;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.MyException;
import service.WarehouseService;

import java.util.List;
import java.util.Optional;


@Path("/products")
public class WarehouseResource {



    private final WarehouseService warehouseService;

    @Inject
    public WarehouseResource(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public WarehouseResource() {
        this.warehouseService = new WarehouseService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        try {
            List<Product> products = warehouseService.getAllProducts();
            return Response.status(Response.Status.OK).entity(products).build();

        } catch (MyException e) {
            // return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured while retriving products").build();
            throw e;
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewProduct(@Valid Product product) {
        try {
            if (product.getName() == null || product.getName().isEmpty()) {
                throw new MyException("product name cannot be empty or null");
            }
            warehouseService.addNewProduct(product);

            return Response.status(Response.Status.CREATED).entity("Product successfully added").build();

        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while adding the products").build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("id") int productId) {
        try {

            Optional<Product> foundProduct = warehouseService.getProductById(productId);

            if (foundProduct.isPresent()) {
                return Response.status(Response.Status.OK).entity(foundProduct.get()).build();
            } else {
                //return Response.status(Response.Status.NOT_FOUND).entity("Product not found with ID: " + productId).build();
                throw new MyException("Invalid id " + productId);
            }
        } catch (Exception e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred: " + e.getMessage()).build();


        }
    }

    @GET
    @Path("/category/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCategory(@PathParam("category") ProductCategory category) {
        try {
            List<Product> products = warehouseService.getProductsByCategory(category);
            if (!products.isEmpty()) {
                return Response.status(Response.Status.OK).entity(products).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No products found for the given category: " + category).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving products by category").build();
        }
    }

}
