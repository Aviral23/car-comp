package com.intuit.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Blob image;
    private Long carId;
    private Boolean defaultImage;

}

