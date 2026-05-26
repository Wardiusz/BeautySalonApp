package com.wardiusz.beautysalonapp.dto.salon.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalonListInfoRequest {

    private String nameOfBusiness;

    private String district;

    private double rating;

    private int amountOfReviews;

    private double price;

}
