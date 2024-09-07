CREATE TABLE `car` (
                       `id` BINARY(16),
                       `make` varchar(100),
                       `model` varchar(100),
                       `name` varchar(255),
                       `manufacture_year` int,
                       `color` varchar(100),
                       `feature_id` int,
                       `specification_id` int,
                       `images` varchar(255),
                       `price` DECIMAL(10, 2),
                       `type` varchar(50),
                       `car_variant` varchar(255),
                       PRIMARY KEY (`id`)
);

CREATE TABLE `feature` (
                           `id` int AUTO_INCREMENT PRIMARY KEY,
                           `has_navigation` bool,
                           `has_bluetooth` bool,
                           `has_rear_camera` bool,
                           `gear_transmission` varchar(30),
                           `width` int,
                           `length` int,
                           `height` int
);

CREATE TABLE `specification` (
                         `id` int AUTO_INCREMENT PRIMARY KEY,
                         `number_of_seats` int,
                         `number_of_airbags` int,
                         `has_abs` bool,
                         `has_adas` bool,
                         `warranty_years` int,
                         `engine_HP` int,
                         `engine_variant` varchar(100)
);

ALTER TABLE `feature` ADD FOREIGN KEY (`id`) REFERENCES `car` (`feature_id`);

ALTER TABLE `specification` ADD FOREIGN KEY (`id`) REFERENCES `car` (`specification_id`);

CREATE INDEX idx_name ON car (name);

CREATE INDEX idx_type_price ON car (type, price);

INSERT INTO
    `specification`
(number_of_seats,number_of_airbags,has_abs,has_adas,warranty_years,engine_HP,engine_variant) VALUES
 (5, 6, true, false, 3, 185, 'Petrol'),
 (7, 10, true, true, 5, 310, 'Diesel'),
 (4, 2, false, false, 1, 140, 'EV');

INSERT INTO
    feature
(has_navigation,has_bluetooth,has_rear_camera,gear_transmission,width,`length`,height) VALUES
(true, true, true, 'automatic', 72, 192, 57),
(false, true, false, 'manual', 70, 177, 54),
(true, true, true, 'automatic', 75, 204, 61),
(true, false, false, 'automatic', 68, 182, 56);


INSERT INTO
    `car`(`id`,make,model,`name`,manufacture_year,color,feature_id,specification_id,images,price,`type`,car_variant) VALUES
    ('50de0707','Toyota', 'Camry', 'Toyota Camry', 2019, 'White', 1, 1, 'image.url.car.jpg', 25000.00, 'sedan', 'LE'),
    ('50de0705','Honda', 'Civic', 'Honda Civic', 2020, 'Black', 2, 2, 'image.url.car2.jpg', 22000.00, 'sedan', 'LX'),
    ('50de0706','Ford', 'Mustang', 'Ford Mustang', 2021, 'Red', 3, 3, 'image.url.car3.jpg', 40000.00, 'coupe', 'GT'),
    ('50de0708','Chevrolet', 'Camaro', 'Chevrolet Camaro', 2020, 'Yellow', 4, 1, 'image.url.car4.jpg', 67000.00, 'coupe', 'ZL1');