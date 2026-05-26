package com.wardiusz.beautysalonapp.service;


import com.wardiusz.beautysalonapp.dto.salon.SalonDto;
import com.wardiusz.beautysalonapp.dto.salon.SalonFilter;

import java.util.List;

public interface SalonService {

    List<SalonDto> getSalons(SalonFilter filter);

    SalonDto updateSalon(Long id, SalonDto dto);

}
