package com.wardiusz.beautysalonapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salons")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nameOfBusiness;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String district;

    private String phone;

    private String website;

    private List<String> services = new ArrayList<>();

    private double priceLow;

    private double priceHigh;

    private double rating;

    private int amountOfReviews;

}
