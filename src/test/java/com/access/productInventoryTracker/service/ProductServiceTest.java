package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import com.access.productInventoryTracker.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private List<Product> mockProducts;

    @BeforeEach
    void setup() {
        mockProducts = Arrays.asList(
                new Product(1L, "Laptop", 1500.0, "Electronics", true),
                new Product(2L, "Smartphone", 800.0, "Electronics", false),
                new Product(3L, "Coffee Maker", 100.0, "Home Appliances", true),
                new Product(4L, "Blender", 150.0, "Home Appliances", true),
                new Product(5L, "T-Shirt", 30.0, "Apparel", true),
                new Product(6L, "Jeans", 45.0, "Apparel", true),
                new Product(7L, "Desk Lamp", 89.99, "Home Appliances", false),
                new Product(8L, "Wall Art", 120.0, "Home Decor", true),
                new Product(9L, "Sneakers", 75.0, "Apparel", true),
                new Product(10L, "Wristwatch", 250.0, "Accessories", false),
                new Product(11L, "Backpack", 60.0, "Accessories", true),
                new Product(12L, "Microwave Oven", 99.0, "Home Appliances", false),
                new Product(13L, "Floor Rug", 150.0, "Home Decor", true),
                new Product(14L, "Speaker", 300.0, "Electronics", true),
                new Product(15L, "E-reader", 200.0, "Electronics", false),
                new Product(16L, "Gaming Console", 499.99, "Electronics", true),
                new Product(17L, "Office Chair", 220.0, "Office Supplies", true),
                new Product(18L, "Pen Set", 29.99, "Office Supplies", true),
                new Product(19L, "Mountain Bike", 489.0, "Outdoor", true),
                new Product(20L, "Camping Tent", 270.0, "Outdoor", false)
        );
    }


    //
    // unite test for the bug related to data transformation
    //
    @Test
    void shouldDemonstrateCategoryTransformationBug() {

        when(productRepository.findAll()).thenReturn(
                List.of(new Product(1L, "Laptop", 1500.0, "Electronics", true))
        );

        List<ProductDTO> result =
                productService.getAllProducts();

        // ❌ Fails BEFORE fix because DTO lowercases category
        assertEquals("Electronics", result.get(0).getCategory());
    }




    // -------------------------
    // Category Filter
    // -------------------------

    @Test
    void shouldReturnProductsFromMultipleCategories() {
        // Filter Electronics and Home Appliances
        when(productRepository.findByCategoryIgnoreCase("Electronics"))
                .thenReturn(mockProducts.stream()
                        .filter(p -> p.getCategory().equalsIgnoreCase("Electronics"))
                        .toList());

        when(productRepository.findByCategoryIgnoreCase("Home Appliances"))
                .thenReturn(mockProducts.stream()
                        .filter(p -> p.getCategory().equalsIgnoreCase("Home Appliances"))
                        .toList());

        List<ProductDTO> electronics = productService.getProductsByCategoryWithoutBug("Electronics");
        List<ProductDTO> appliances = productService.getProductsByCategoryWithoutBug("Home Appliances");

        assertEquals(5, electronics.size());
        assertEquals(4, appliances.size());
    }

    @Test
    void shouldFilterProductsUsingSpecificationBuilderByPriceRange() {
        List<Product> inRange = mockProducts.stream()
                .filter(p -> p.getPrice() >= 100 && p.getPrice() <= 300)
                .toList();

        when(productRepository.findAll(any(Specification.class)))
                .thenReturn(inRange);

        List<ProductDTO> result = productService.filterProductsUsingSpecificationBuilder(null, null, 100.0, 300.0, null);

        assertEquals(9, result.size());
        result.forEach(p -> assertTrue(p.getPrice() >= 100 && p.getPrice() <= 300));
    }

    @Test
    void shouldFilterProductsUsingSpecificationBuilderByCategoryAndPriceAndAvailability() {
        List<Product> filtered = mockProducts.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Electronics"))
                .filter(p -> p.getPrice() >= 200 && p.getPrice() <= 500)
                .filter(Product::isAvailable)
                .toList();

        when(productRepository.findAll(any(Specification.class)))
                .thenReturn(filtered);

        List<ProductDTO> result = productService.filterProductsUsingSpecificationBuilder(
                null, "Electronics", 200.0, 500.0, true
        );

        assertEquals(2, result.size());
        result.forEach(p -> {
            assertEquals("Electronics", p.getCategory());
            assertTrue(p.getPrice() >= 200 && p.getPrice() <= 500);
            assertTrue(p.isAvailable());
        });
    }


    @Test
    void shouldReturnProductsByName() {
        when(productRepository.findByNameContainingIgnoreCase("lap"))
                .thenReturn(List.of(
                        mockProducts.get(0)
                ));

        List<ProductDTO> result = productService.getProductsByName("lap");

        assertEquals(1, result.size());

        result.forEach(p ->
                assertTrue(p.getName().toLowerCase().contains("lap"))
        );
    }

    @Test
    void shouldReturnProductsByNameContaining() {
        when(productRepository.findByNameContainingIgnoreCase("maker"))
                .thenReturn(List.of(
                        mockProducts.get(2)
                ));

        List<ProductDTO> result = productService.getProductsByName("maker");

        assertEquals(1, result.size());

        result.forEach(p ->
                assertTrue(p.getName().toLowerCase().contains("maker"))
        );
    }




    @Test
    void shouldReturnProductsByNamePartialMatch() {
        // Search for products containing "watch" (matches "Wristwatch")
        when(productRepository.findByNameContainingIgnoreCase("watch"))
                .thenReturn(List.of(
                        mockProducts.get(9)  // Wristwatch
                ));

        List<ProductDTO> result = productService.getProductsByName("watch");

        assertEquals(1, result.size());
        assertEquals("Wristwatch", result.get(0).getName());
    }

    @Test
    void shouldReturnEmptyIfNoProductMatchesName() {
        // Search for a name that doesn't exist
        when(productRepository.findByNameContainingIgnoreCase("xyz"))
                .thenReturn(List.of());

        List<ProductDTO> result = productService.getProductsByName("xyz");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnProductsByCategory() {

        List<Product> electronics = mockProducts.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Electronics"))
                .toList();

        when(productRepository.findByCategoryIgnoreCase("Electronics"))
                .thenReturn(electronics);

        List<ProductDTO> result =
                productService.getProductsByCategoryWithoutBug("Electronics");

        assertEquals(5, result.size());
        result.forEach(p ->
                assertEquals("Electronics", p.getCategory()));
    }

    @Test
    void shouldReturnEmptyForUnknownCategory() {

        when(productRepository.findByCategoryIgnoreCase("Toys"))
                .thenReturn(List.of());

        List<ProductDTO> result =
                productService.getProductsByCategoryWithoutBug("Toys");

        assertTrue(result.isEmpty());
    }

    // -------------------------
    // Price Range Filter
    // -------------------------

    @Test
    void shouldReturnProductsWithinPriceRange() {

        List<Product> inRange = mockProducts.stream()
                .filter(p -> p.getPrice() >= 100 && p.getPrice() <= 300)
                .toList();

        when(productRepository.findByPriceBetween(100, 300))
                .thenReturn(inRange);

        List<ProductDTO> result =
                productService.getProductsByPriceRange(100, 300);

        assertFalse(result.isEmpty());
        result.forEach(p ->
                assertTrue(p.getPrice() >= 100 && p.getPrice() <= 300));
    }

    @Test
    void shouldReturnEmptyWhenNoProductInRange() {

        when(productRepository.findByPriceBetween(2000, 3000))
                .thenReturn(List.of());

        List<ProductDTO> result =
                productService.getProductsByPriceRange(2000, 3000);

        assertTrue(result.isEmpty());
    }

    // -------------------------
    // Availability Filter
    // -------------------------

    @Test
    void shouldReturnOnlyAvailableProducts() {

        List<Product> available = mockProducts.stream()
                .filter(Product::isAvailable)
                .toList();

        when(productRepository.findByAvailable(true))
                .thenReturn(available);

        List<ProductDTO> result =
                productService.getProductsByAvailability(true);

        assertFalse(result.isEmpty());
        result.forEach(ProductDTO::isAvailable);
    }

    @Test
    void shouldReturnOnlyUnavailableProducts() {

        List<Product> unavailable = mockProducts.stream()
                .filter(p -> !p.isAvailable())
                .toList();

        when(productRepository.findByAvailable(false))
                .thenReturn(unavailable);

        List<ProductDTO> result =
                productService.getProductsByAvailability(false);

        assertFalse(result.isEmpty());
        result.forEach(p -> assertFalse(p.isAvailable()));
    }
}
