package com.wardiusz.beautysalonapp.service.impl;

import com.wardiusz.beautysalonapp.dto.salon.SalonDto;
import com.wardiusz.beautysalonapp.dto.salon.SalonFilter;
import com.wardiusz.beautysalonapp.entity.Salon;
import com.wardiusz.beautysalonapp.exception.GlobalException;
import com.wardiusz.beautysalonapp.mapper.SalonMapper;
import com.wardiusz.beautysalonapp.repository.SalonRepository;
import com.wardiusz.beautysalonapp.repository.SalonSpecification;
import com.wardiusz.beautysalonapp.service.SalonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public List<SalonDto> getSalons(SalonFilter filter) {
        return SalonMapper.toDTOList(salonRepository.findAll(SalonSpecification.withFilter(filter)));
    }

    @Transactional
    @Override
    public SalonDto updateSalon(Long id, SalonDto dto) {
        Salon salon = getSalonById(id);
        SalonMapper.updateEntity(salon, dto);

        return SalonMapper.toDto(salonRepository.save(salon));
    }

    public Salon getSalonById(Long id) {
        return salonRepository.findById(id)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Salon not found."));
    }
}
