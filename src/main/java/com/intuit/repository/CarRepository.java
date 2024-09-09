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

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findTop10ByTypeContainingAndPriceLessThanEqualOrderByPriceDesc(String type, BigDecimal price, Pageable pageable);
    Optional<Car> findById(@Param("id") Long id);

    @Query("SELECT c FROM Car c WHERE c.id != :currentCarId " +
            "ORDER BY CASE WHEN c.make = :currentCarMake THEN 0 ELSE 1 END, " +
            "CASE WHEN c.type = :currentCarType THEN 0 ELSE 1 END, " +
            "ABS(c.price - :currentCarPrice) ASC")
    List<Car> getTop10SimilarCars(@Param("currentCarId") Long currentCarId,
                                  @Param("currentCarMake") String currentCarMake,
                                  @Param("currentCarType") String currentCarType,
                                  @Param("currentCarPrice") BigDecimal currentCarPrice, Pageable pageable);

    List<Car> findTop10ByNameContaining(String name);

    @Query(value="Select f.id, f.has_bluetooth, f.has_navigation, f.has_rear_camera, f.gear_transmission from feature f where f.id IN (Select c.feature_id from car c where c.id IN :idList)", nativeQuery = true)
    List<Object[]> findFeature(@Param("idList") List<Long> idList);

    @Query(value="Select s.id, s.engine_HP, s.has_abs, s.has_adas, s.number_of_airbags, s.number_of_seats, s.warranty_years, s.engine_variant from specification s where s.id IN (Select c.specification_id from car c where c.id IN :idList)", nativeQuery = true)
    List<Object[]> findSpecs(@Param("idList") List<Long> idList);
}

