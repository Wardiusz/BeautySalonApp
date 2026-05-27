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

    @ElementCollection
    @CollectionTable(name = "salon_services", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "name")
    private List<String> services;

    private double priceLow;

    private double priceHigh;

    private double rating;

    private int amountOfReviews;

}
