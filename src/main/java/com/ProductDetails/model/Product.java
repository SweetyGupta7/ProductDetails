package com.ProductDetails.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("minPrice")
    private int minPrice;

    @JsonProperty("price")
    private int price;


    @JsonProperty("maxPrice")
    private int maxPrice;

    @JsonProperty("minPostedDate")
    private String minPostedDate;

    @JsonProperty("maxPostedDate")
    private String maxPostedDate;

    @JsonProperty("status")
    private String status;
}
