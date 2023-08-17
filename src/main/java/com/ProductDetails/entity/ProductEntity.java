package com.ProductDetails.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private  int price;


    @Column(name = "status")
    private String status;


    @Column(name = "created_at")
    private Date createdAt = new Date();


    @Column(name = "updated_at")
    private Date updatedAt = new Date();
}
