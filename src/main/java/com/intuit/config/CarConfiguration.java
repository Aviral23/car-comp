package com.intuit.config;

import com.intuit.models.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.*;


@Configuration
public class CarConfiguration {
    @Bean
    public HashMap<String, Car> carHashMap() {
        HashMap<String, Car> carMap = new HashMap<>();

        /*Random random = new Random();
        List<Image> images;
        for (int i = 1; i <= 80; i++) {
            String id = String.format("%04d", i);

            // Generate random car details for each car
            String make = generateRandomMake(random);
            String model = generateRandomModel(random);
            int year = 2021 - random.nextInt(10);
            String color = generateRandomColor(random);
            Feature features = generateRandomFeature(random);
            Specifications specifications = generateRandomSpec(random);
            Image image1 = new Image("https://car-images.com/1/" + make + "/" + model);
            Image image2 = new Image("https://car-images.com/2/" + make + "/" + model);
            images = new ArrayList<>(Arrays.asList(image1, image2));
            BigDecimal price = new BigDecimal("10000");
            String type = generateRandomType(random);

            Car car = new Car(id, make, model, year, color, features, specifications, images, price, type);
            carMap.put(id, car);

        }
        //testing init
        carMap.forEach((key, car) -> {
            System.out.println("Key: " + key);
            System.out.println("Value: " + car.toString());
        });*/
        return carMap;
    }

    // Helper methods to generate random car properties
    private static String generateRandomMake(Random random) {
        String[] makes = {"Toyota", "Ford", "Honda", "Chevrolet", "BMW", "Mercedes-Benz", "Audi", "Volkswagen",
                "Jeep", "Dodge", "Hyundai", "Subaru", "Kia", "Lexus", "Mazda", "Nissan"};
        return makes[random.nextInt(makes.length)];
    }

    private static String generateRandomModel(Random random) {
        String[] models = {"Camry", "Corolla", "Mustang", "F-150", "Civic", "Accord", "Impala", "Tahoe", "X5", "C-Class",
                "GLC-Class", "A3", "Passat", "Wrangler", "Challenger", "Santa Fe", "Outback", "Soul", "ES 350", "CX-5", "Altima"};
        return models[random.nextInt(models.length)];
    }

    private static String generateRandomColor(Random random) {
        String[] colors = {"Red", "Blue", "Green", "Yellow", "Black", "White", "Silver", "Gray", "Brown", "Orange"};
        return colors[random.nextInt(colors.length)];
    }

    private static Feature generateRandomFeature(Random random) {
        boolean hasNavigation = random.nextBoolean();
        boolean hasBluetooth = random.nextBoolean();
        boolean hasRearCamera = random.nextBoolean();
        GearTransmission gearTransmission = generateRandomTransmission(random);
        Dimension dimension = generateRandomDimension(random);
        return new Feature(hasNavigation, hasBluetooth, hasRearCamera, gearTransmission, dimension);
    }

    private static GearTransmission generateRandomTransmission(Random random) {
        return random.nextBoolean() ? GearTransmission.AUTO : GearTransmission.MANUAL;
    }

    private static Dimension generateRandomDimension(Random random) {
        double length = 100.0 + random.nextInt(200);
        double width = 50.0 + random.nextInt(100);
        double height = 30.0 + random.nextInt(70);
        return new Dimension(length, width, height);
    }

    private static Specifications generateRandomSpec(Random random) {
        int numberOfSeats = random.nextInt(7) + 2; // generate a random number of seats between 2 and 8
        int warrantyYears = 2 + random.nextInt(4); // generate a random warranty period between 2 and 5 years
        int numberOfAirbags = random.nextInt(5);
        String engineHP = generateRandomEngineHP(random);
        String variant = generateRandomVariant(random);
        boolean hasABS = random.nextBoolean();
        boolean hasADAS = random.nextBoolean();
//        return new Specifications(numberOfSeats, warrantyYears, engineHP, variant, hasADAS, hasABS, numberOfAirbags);
        return new Specifications();
    }

    private static String generateRandomEngineHP(Random random) {
        int horsepower = 100 + random.nextInt(400); // generate a random horsepower between 100 and 500
        int cylinders = 4 + random.nextInt(6); // generate a random number of cylinders between 4 and 9
        return horsepower + " HP, " + cylinders + " cylinders";
    }

    private static String generateRandomVariant(Random random) {
        String[] variants = {"Standard", "Deluxe", "Sport", "Limited"};
        int index = random.nextInt(variants.length);
        return variants[index];
    }

    private static String generateRandomType(Random random) {
        String[] types = {"Sedan", "Coupe", "Sports car", "SUV", "Hatchback", "Pickup", "Van", "Convertible", "Wagon"};
        return types[random.nextInt(types.length)];
    }
}
