package com.ProductDetails.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@ToString
@Entity
@Table(name = "ProductQueue")
public class ProductQueueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "id")
    private int id;

    @Column(name = "status")
    private String status;

    @Column(name = "request_type")
    private String request_type;

    @Column(name = "product_id")
    private int product_id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();
}
