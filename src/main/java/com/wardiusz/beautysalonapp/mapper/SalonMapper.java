package com.wardiusz.beautysalonapp.mapper;

import com.wardiusz.beautysalonapp.dto.salon.SalonDto;
import com.wardiusz.beautysalonapp.entity.Salon;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SalonMapper {
    public SalonDto toDto(Salon salon) {
        if (salon == null) return null;
        return SalonDto.builder()
                .id(salon.getId())
                .nameOfBusiness(salon.getNameOfBusiness())
                .address(salon.getAddress())
                .district(salon.getDistrict())
                .phone(salon.getPhone())
                .website(salon.getWebsite())
                .services(salon.getServices())
                .priceLow(salon.getPriceLow())
                .priceHigh(salon.getPriceHigh())
                .rating(salon.getRating())
                .amountOfReviews(salon.getAmountOfReviews())
                .build();
    }

    public void updateEntity(Salon salon, SalonDto dto) {
        if (salon == null || dto == null) return;
        salon.setNameOfBusiness(dto.getNameOfBusiness());
        salon.setAddress(dto.getAddress());
        salon.setDistrict(dto.getDistrict());
        salon.setPhone(dto.getPhone());
        salon.setWebsite(dto.getWebsite());
        salon.setServices(dto.getServices());
        salon.setPriceLow(dto.getPriceLow());
        salon.setPriceHigh(dto.getPriceHigh());
        salon.setRating(dto.getRating());
        salon.setAmountOfReviews(dto.getAmountOfReviews());
    }

    public List<SalonDto> toDTOList(List<Salon> salons) {
        return salons.stream()
                .map(SalonMapper::toDto)
                .toList();
    }
}
