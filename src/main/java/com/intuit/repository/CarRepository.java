package com.intuit.repository;

import com.intuit.models.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    @Query("SELECT c FROM Car c WHERE c.type = :type AND c.price <= :price ORDER BY c.price DESC")
    List<Car> findTop10ByTypeAndPriceLessThanEqualOrderByPriceDesc(@Param("type") String type, @Param("price") BigDecimal price, Pageable pageable);

    Optional<Car> findById(@Param("id") UUID id);

    @Query("SELECT c FROM Car c WHERE c.id != :currentCarId " +
            "ORDER BY CASE WHEN c.make = :currentCarMake THEN 0 ELSE 1 END, " +
            "CASE WHEN c.type = :currentCarType THEN 0 ELSE 1 END, " +
            "ABS(c.price - :currentCarPrice) ASC LIMIT 10")
    List<Car> getTop10SimilarCars(@Param("currentCarId") UUID currentCarId,
                                  @Param("currentCarMake") String currentCarMake,
                                  @Param("currentCarType") String currentCarType,
                                  @Param("currentCarPrice") BigDecimal currentCarPrice);


    @Query("SELECT c FROM Car c WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%')")
    Optional<Car> findByName(@Param("name") String name);
}

