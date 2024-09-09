package com.intuit.config;

import com.intuit.models.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.lang.*;

import static com.intuit.config.CarConfiguration.generateRandomTransmission;

class DBInsertsHelper
{
    public static void main (String[] args) throws java.lang.Exception
    {
        HashMap<Long, Car> carMap = new HashMap<>();
        // INSERT INTO `car` (make,model,`name`,manufacture_year,color,feature_id,specification_id,price,`type`,car_variant) VALUES ('Toyota', 'Camry', 'Toyota Camry', 2019, 'White', 8, 7, 25000.00, 'sedan', 'LE');

        Car car = null;
        Feature feature = null;
        Specification specification = null;
        Random random = new Random();
        // List<Image> images;

        for (int i = 1; i <= 80; i++) {
            Long id = 10000L+i;

            // Generate random car details for each car
            String make = generateRandomMake(random);
            String model = generateRandomModel(random);
            String name = make + " " + model;
            int year = 2024 - random.nextInt(8);
            String color = generateRandomColor(random);
            // Feature features = generateRandomFeature(random);
            // Specifications specifications = generateRandomSpec(random);
            // Image image1 = new Image("https://car-images.com/1/" + make + "/" + model);
            // Image image2 = new Image("https://car-images.com/2/" + make + "/" + model);
            // images = new ArrayList<>(Arrays.asList(image1, image2));
            BigDecimal min = new BigDecimal("20000.00");
            BigDecimal max = new BigDecimal("90000");
            BigDecimal randomBigDecimal = min.add(new BigDecimal(String.valueOf(random.nextDouble()))
                            .multiply(max.subtract(min)))
                    .setScale(2, RoundingMode.HALF_UP);
            String type = generateRandomType(random);
            feature = generateRandomFeature(1200+i, random);
            specification = generateRandomSpec(1200+i, random);
            car = new Car(make, model, name, year, color, feature, specification, null, randomBigDecimal, type, generateRandomVariant(random));
            carMap.put(id, car);

        }
        //testing init
        System.out.println("INSERT INTO `specification` (id,number_of_seats,warranty_years,engine_HP,engine_variant,has_adas,has_abs,number_of_airbags) VALUES ");
        carMap.forEach((key, x) -> {
            Specification l = x.getSpecification();
            System.out.print("(");
            System.out.print(l.getId() + ","+l.getNumberOfSeats()+","+l.getWarrantyYears()+",'"+l.getEngineHP()+"','"+
                    l.getEngineVariant()+"',"+l.getHasADAS()+","+l.getHasABS()+","+l.getNumberOfAirbags());
            System.out.print("),");
        });
        System.out.println();
        System.out.println("INSERT INTO `feature` (id, has_navigation,has_bluetooth,has_rear_camera,gear_transmission,width,`length`,height) VALUES ");
        carMap.forEach((key, x) -> {
            Feature l = x.getFeature();
            System.out.print("(");
            System.out.print(l.getId()+","+l.getHasNavigation()+","+l.getHasBluetooth()+","+l.getHasRearCamera()+",'"+l.getGearTransmission()+"',"+
                    40+","+42+","+29);
            System.out.print("),");
        });
        System.out.println();
        System.out.println("INSERT INTO `car` (id, make,model,`name`,manufacture_year,color,feature_id,specification_id,price,`type`,car_variant) VALUES ");
        carMap.forEach((key, x) -> {
            System.out.print("(");
            System.out.print(key+",'"+x.getMake()+"','"+x.getModel()+"','"+x.getName()+"',"+x.getManufactureYear()+",'"+x.getColor()+"',"+
                    x.getFeature().getId()+","+x.getSpecification().getId()+","+x.getPrice()+",'"+x.getType()+"','"+x.getCarVariant()+"'");
            System.out.print("),");
        });

        System.out.println();
        System.out.println("INSERT INTO `image` (car_id, default_image, image) VALUES ");
        carMap.forEach((key, x) -> {
            System.out.print("(");
            System.out.print(key + "," + true + ",'" + "FFD8FFE000104A4649460001" + "'");
            System.out.print("),");
        });


        // System.out.println()
        // return carMap;
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

     private static Feature generateRandomFeature(int id, Random random) {
         boolean hasNavigation = random.nextBoolean();
         boolean hasBluetooth = random.nextBoolean();
         boolean hasRearCamera = random.nextBoolean();
         GearTransmission gearTransmission = generateRandomTransmission(random);
         return new Feature(id, hasNavigation, hasBluetooth, hasRearCamera, gearTransmission);
     }


//     private static Dimension generateRandomDimension(Random random) {
//         double length = 100.0 + random.nextInt(200);
//         double width = 50.0 + random.nextInt(100);
//         double height = 30.0 + random.nextInt(70);
//         return new Dimension(length, width, height);
//     }

     private static Specification generateRandomSpec(int id, Random random) {
         int numberOfSeats = random.nextInt(7) + 2; // generate a random number of seats between 2 and 8
         int warrantyYears = 2 + random.nextInt(4); // generate a random warranty period between 2 and 5 years
         int numberOfAirbags = random.nextInt(5);
         String engineHP = generateRandomEngineHP(random);
//         String variant = generateRandomVariant(random);
         boolean hasABS = random.nextBoolean();
         boolean hasADAS = random.nextBoolean();
         return new Specification(id, numberOfSeats, warrantyYears, engineHP, EngineVariant.PETROL, hasADAS, hasABS, numberOfAirbags);
//         return new Specification();
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
