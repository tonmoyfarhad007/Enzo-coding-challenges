package com.access.productInventoryTracker.repository.specification;

import com.access.productInventoryTracker.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null :
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                category == null ? null :
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                (minPrice == null || maxPrice == null) ? null :
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }

    public static Specification<Product> isAvailable(Boolean available) {
        return (root, query, criteriaBuilder) ->
                available == null ? null :
                criteriaBuilder.equal(root.get("available"), available);
    }
}
