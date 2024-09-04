package com.intuit.repository;

import com.intuit.models.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {
    @Query("{'type': ?0, 'price': { $lte: ?1 }}")
    List<Car> findTop10ByTypeAndPriceLessThanEqualOrderByPriceDesc(String type, double price, Pageable pageable);

}

