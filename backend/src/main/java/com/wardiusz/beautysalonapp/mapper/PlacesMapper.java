package com.wardiusz.beautysalonapp.mapper;

import com.wardiusz.beautysalonapp.dto.places.PlacesAddressComponent;
import com.wardiusz.beautysalonapp.dto.places.PlacesDto;
import com.wardiusz.beautysalonapp.entity.Salon;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PlacesMapper {

    private static final Set<String> RELEVANT_TYPES = Set.of(
            "hair_salon", "beauty_salon", "nail_salon",
            "spa", "barber_shop", "skin_care_clinic"
    );

    public Salon toSalon(PlacesDto place) {
        if (place == null || place.getDisplayName() == null) return null;

        double[] priceRange = mapPriceLevel(place.getPriceLevel());

        return Salon.builder()
                .nameOfBusiness(place.getDisplayName().getText())
                .address(place.getFormattedAddress() != null ? place.getFormattedAddress() : "Warszawa")
                .district(extractDistrict(place.getAddressComponents()))
                .phone(place.getNationalPhoneNumber())
                .website(place.getWebsiteUri())
                .services(mapTypes(place.getTypes()))
                .priceLow(priceRange[0])
                .priceHigh(priceRange[1])
                .rating(place.getRating())
                .amountOfReviews(place.getUserRatingCount())
                .build();
    }

    public List<Salon> toSalonList(List<PlacesDto> places) {
        return places.stream()
                .map(PlacesMapper::toSalon)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String extractDistrict(List<PlacesAddressComponent> components) {
        if (components == null) return "Warszawa";
        return components.stream()
                .filter(c -> c.getTypes() != null &&
                        (c.getTypes().contains("sublocality_level_1") ||
                                c.getTypes().contains("sublocality")))
                .map(PlacesAddressComponent::getLongText)
                .findFirst()
                .orElse("Warszawa");
    }

    private double[] mapPriceLevel(String priceLevel) {
        if (priceLevel == null)
            return new double[]{0, 0};

        return switch (priceLevel) {
                    case "PRICE_LEVEL_INEXPENSIVE" -> new double[]{50, 100};
                    case "PRICE_LEVEL_MODERATE" -> new double[]{100, 200};
                    case "PRICE_LEVEL_EXPENSIVE" -> new double[]{200, 400};
                    case "PRICE_LEVEL_VERY_EXPENSIVE"-> new double[]{400, 999};
                    default -> new double[]{0,   0};
                };
    }

    private List<String> mapTypes(List<String> types) {
        if (types == null) return List.of();
        return types.stream()
                .filter(RELEVANT_TYPES::contains)
                .map(t -> t.replace("_", " "))
                .collect(Collectors.toList());
    }
}