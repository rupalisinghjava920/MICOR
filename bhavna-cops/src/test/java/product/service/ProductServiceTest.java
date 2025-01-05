package product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import product.service.entity.Product;
import product.service.repository.ProductRepository;
import product.service.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
   public void testCreateProduct() {
        Product product = new Product(1L, "Laptop", 1200.00);
        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct);
        assertEquals("Laptop", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Laptop", 1200.00));
        products.add(new Product(2L, "Phone", 800.00));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> allProducts = productService.getAllProducts();

        assertEquals(2, allProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById(){

        Product product=new Product(1l,"Laptop",1200.00);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> product1=productService.getProductById(1L);

        assertTrue(product1.isPresent());
        assertEquals("Laptop",product1.get().getName());
        verify(productRepository,times(1)).findById(1L);
    }

    @Test
    public void testUpdateProduct(){
        Product existingProduct=new Product(1L,"Laptop",1200.00);
        Product updateProduct=new Product(1L,"Phone",2000.00);

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(updateProduct)).thenReturn(updateProduct);

        Product result=productService.updateProduct(1L,updateProduct);

        assertNotNull(result);
        assertEquals("Phone",result.getName());
        assertEquals(2000.00,result.getPrice());
        verify(productRepository,times(1)).existsById(1L);
        verify(productRepository,times(1)).save(updateProduct);

    }


    @Test
    void testDeleteProductExists() {
            when(productRepository.existsById(1L)).thenReturn(true);

            boolean isDeleted = productService.deleteProduct(1L);

            assertTrue(isDeleted);
            verify(productRepository, times(1)).existsById(1L);
            verify(productRepository, times(1)).deleteById(1L);
    }


    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        boolean isDeleted = productService.deleteProduct(1L);

        assertFalse(isDeleted);
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(1L);
    }
}
