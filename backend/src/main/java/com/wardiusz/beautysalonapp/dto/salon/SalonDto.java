package com.wardiusz.beautysalonapp.dto.salon;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalonDto {

    private Long id;

    @NotBlank
    private String nameOfBusiness;

    @NotBlank
    private String address;

    @NotBlank
    private String district;

    private String phone;

    private String website;

    private List<String> services;

    private double priceLow;

    private double priceHigh;

    private double rating;

    private int amountOfReviews;

}
