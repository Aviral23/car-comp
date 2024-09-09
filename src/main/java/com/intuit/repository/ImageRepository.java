package com.intuit.repository;

import com.intuit.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository  extends JpaRepository<Image, Long> {

    @Query(value = "select i.id, i.image, i.car_id, i.default_image from image i where car_id=:id and default_image=:defaultImage LIMIT 1", nativeQuery = true)
    List<Object[]> findByCarIdAndDefaultImage(Long id, Boolean defaultImage);

    @Query(value = "select i.id, i.image, i.car_id, i.default_image from image i where car_id=:id", nativeQuery = true)
    List<Object[]> findByCarId(Long id);
}
