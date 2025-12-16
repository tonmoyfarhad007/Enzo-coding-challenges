package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import com.access.productInventoryTracker.repository.ProductRepository;
import com.access.productInventoryTracker.repository.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                // product.getCategory().toLowerCase(), // ❌ BUG
                product.getCategory(),
                product.isAvailable()
        );
    }


    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // Price Range Filter
    public List<ProductDTO> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // Category Filter
    public List<ProductDTO> getProductsByCategoryWithoutBug(String category) {
        return productRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }



    // Availability Filter
    public List<ProductDTO> getProductsByAvailability(boolean available) {
        return productRepository.findByAvailable(available)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Search product by name
    public List<ProductDTO> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // filter product using specificationBuilder
    public List<ProductDTO> filterProductsUsingSpecificationBuilder(String name, String category, Double minPrice, Double maxPrice, Boolean available) {
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(ProductSpecification.isAvailable(available));

        return productRepository.findAll(spec)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // AI Generated
    public List<ProductDTO> getProductsByCategoryWithBug(String category) {
        return productRepository.findAll().stream()
                .flatMap(product -> {
                    if (!product.getCategory().equalsIgnoreCase(category)) {
                        return Stream.of(convertToDTO(product)); // ❌ inverted condition
                    }
                    return Stream.empty();
                })
                .collect(Collectors.toList());
    }

}
