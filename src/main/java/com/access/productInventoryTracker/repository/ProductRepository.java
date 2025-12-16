package com.access.productInventoryTracker.repository;

import com.access.productInventoryTracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // You can add custom methods here if needed, for example: 
    // List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByAvailable(boolean available);

    List<Product> findByNameContainingIgnoreCase(String name);


    // AI Generated
//    @Query(value = "SELECT * FROM product p " +
//                   "WHERE p.category = '" + ":#{#category}" + "' " +
//                   "AND p.available = true " +
//                   "ORDER BY p.price DESC",
//           nativeQuery = true)
//    List<Product> findProductsByCategory(@Param("category") String category);
}
