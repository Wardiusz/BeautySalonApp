package com.wardiusz.beautysalonapp.dto.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesDto {

    private String id;

    private PlacesDisplayName displayName;

    private String formattedAddress;

    private String nationalPhoneNumber;

    private String websiteUri;

    private double rating;

    private int userRatingCount;

    private String priceLevel;

    private List<String> types;

    private List<PlacesAddressComponent> addressComponents;

}
