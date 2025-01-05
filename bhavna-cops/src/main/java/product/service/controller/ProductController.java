package product.service.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.service.customeException.GlobelExceptioHandle;
import product.service.entity.Product;
import product.service.service.ProductService;
import product.service.unit.Constant;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/healthCheck")
    @Operation(summary = "Check Health of Product Service")
    @ApiResponse(responseCode = "200", description = "Service is working fine")
    public String healthCheck() {
        return "Product service working fine";
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        try {
           if(createdProduct!= null){
               return ResponseEntity.status(HttpStatus.OK).body(Constant.SAVE_DATA);
           }
        }catch (Exception e){
            throw new RuntimeException(Constant.PRODUCT_NOT_SAVE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Constant.SAVE_DATA);

    }

    @GetMapping
    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "List of all products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
//        Optional<Product> product = productService.getProductById(id);
//        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                return new ResponseEntity<>(product.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Constant.PRODUCT_ID);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constant.PRODUCT_GET);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
//        Product updatedProduct = productService.updateProduct(id, product);
//        return updatedProduct != null ? new ResponseEntity<>(updatedProduct, HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

        try {
            Product updatedProduct = productService.updateProduct(id, product);
            if (updatedProduct != null) {
                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Constant.PRODUCT_ID);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constant.PRODUCT_UPDATE);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
//        return productService.deleteProduct(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

        try {
            boolean isDeleted = productService.deleteProduct(id);
            if (isDeleted) {
                return new ResponseEntity<>(Constant.DELETE_ID, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Constant.PRODUCT_ID, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(Constant.PRODUCT_DELETE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/name/{name}")
    @Operation(summary = "Get product name")
    @ApiResponse(responseCode = "200" ,description = "List of product name")
    public ResponseEntity<?> getProductsByName(@PathVariable String name) {

        try {
            List<Product> products = productService.getProductByName(name);
            if (products.isEmpty()) {
                throw new GlobelExceptioHandle(Constant.PRODUCT_NOT_FOUND);
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constant.PRODUCT_NAME_RELATED);
        }

    }

}