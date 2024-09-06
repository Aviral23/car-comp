package com.intuit.repository;

import com.intuit.config.CarConfiguration;
import com.intuit.models.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CarRepositoryImpl implements CarRepository{

    @Autowired
    CarConfiguration carConfiguration;
    @Override
    public List<Car> findTop10ByTypeAndPriceLessThanEqualOrderByPriceDesc(String type, Double price, Pageable pageable) {
        if(price == null)
            price = Double.MAX_VALUE;
        Double finalPrice = price;
        return carConfiguration.carHashMap().values().stream()
                .filter(car -> car.getType().equals(type) && car.getPrice() <= finalPrice)
                .sorted(Comparator.comparingDouble(Car::getPrice).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> getTop10SimilarCars(Car car) {
        Map<String, Car> carMap = carConfiguration.carHashMap();
        PriorityQueue<Car> pq = new PriorityQueue<>(10,
                Comparator.comparingDouble(c -> manhattanDistance(car, c)));
        for (Car c : carMap.values()) {
            pq.offer(c);
            if (pq.size() > 10) {
                pq.poll();
            }
        }
        List<Car> top10Cars = new ArrayList<>(pq);
        top10Cars.sort(Comparator.comparingDouble(c -> manhattanDistance(car, c)));
        return top10Cars;
    }

    public static double manhattanDistance(Car car1, Car car2) {
        double distance = 0.0;
        distance += Math.abs(car1.getManufactureYear() - car2.getManufactureYear());
        distance += Math.abs(car1.getPrice() - car2.getPrice());
        if (!car1.getMake().equals(car2.getMake())) {
            distance += 1.0;
        }
        if (!car1.getModel().equals(car2.getModel())) {
            distance += 1.0;
        }
        if (!car1.getColor().equals(car2.getColor())) {
            distance += 1.0;
        }
        if (!car1.getType().equals(car2.getType())) {
            distance += 1.0;
        }
        return distance;
    }

    @Override
    public Car findById(String id) {
        return carConfiguration.carHashMap().get(id);
    }

}
