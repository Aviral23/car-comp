package com.intuit.repository;

import com.intuit.models.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CarRepository {
    List<Car> findTop10ByTypeAndPriceLessThanEqualOrderByPriceDesc(String type, Double price, Pageable pageable);

    Car findById(String id);

    List<Car> getTop10SimilarCars(Car car);

}

