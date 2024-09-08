Drop table car_images;
Drop table car;
create table IF NOT EXISTS car ( id bigint primary key AUTO_INCREMENT, car_variant varchar(255) null, color varchar(255) null, feature_id int not null, make varchar(255) null, manufacture_year int not null, model varchar(255) null, name varchar(255) null, price decimal(19, 2) not null, specification_id int not null, type varchar(255) not null );

create index idx_name on car (name);

create index idx_type_price on car (type, price);

CREATE TABLE IF NOT EXISTS `feature` ( `id` int AUTO_INCREMENT PRIMARY KEY, `has_navigation` bool, `has_bluetooth` bool, `has_rear_camera` bool, `gear_transmission` varchar(30), `width` int, `length` int, `height` int );

CREATE TABLE IF NOT EXISTS `specification` ( `id` int AUTO_INCREMENT PRIMARY KEY, `number_of_seats` int, `number_of_airbags` int, `has_abs` bool, `has_adas` bool, `warranty_years` int, `engine_HP` varchar(50), `engine_variant` varchar(100) );

alter table car modify id bigint auto_increment;

alter table car auto_increment = 100;

ALTER TABLE car ADD FOREIGN KEY (feature_id) REFERENCES feature(id);

ALTER TABLE `car` ADD FOREIGN KEY (specification_id) REFERENCES `specification` (`id`);

CREATE INDEX idx_name ON car (name);

CREATE INDEX idx_type_price ON car (type, price);

INSERT INTO image (image) VALUES ('car-pictures.com/image1.jpg'), ('car-pictures.com/image2.jpg'), ('car-pictures.com/image3.jpg'), ('car-pictures.com/image4.jpg'), ('car-pictures.com/image5.jpg');

INSERT INTO `specification` (number_of_seats,number_of_airbags,has_abs,has_adas,warranty_years,engine_HP,engine_variant) VALUES (5, 6, true, false, 3, '185 hp', 'PETROL'), (7, 10, true, true, 5, 310, 'DIESEL'), (4, 2, false, false, 1, '140 hp', 'EV'), (4, 3, true, false, 3, '140 hp', 'PETROL');

INSERT INTO feature (has_navigation,has_bluetooth,has_rear_camera,gear_transmission,width,`length`,height) VALUES (true, true, true, 'AUTOMATIC', 72, 192, 57), (false, true, false, 'MANUAL', 70, 177, 54), (true, true, true, 'AUTOMATIC', 75, 204, 61), (true, false, false, 'AUTOMATIC', 68, 182, 56);

INSERT INTO `car` (make,model,`name`,manufacture_year,color,feature_id,specification_id,price,`type`,car_variant) VALUES ('Toyota', 'Camry', 'Toyota Camry', 2019, 'White', 8, 7, 25000.00, 'sedan', 'LE'), ('Honda', 'Civic', 'Honda Civic', 2020, 'Black', 5, 5,  22000.00, 'sedan', 'LX'), ('Ford', 'Mustang', 'Ford Mustang', 2021, 'Red', 6, 6, 40000.00, 'coupe', 'GT'), ('Chevrolet', 'Camaro', 'Chevrolet Camaro', 2020, 'Yellow', 7, 8, 67000.00, 'coupe', 'ZL1');