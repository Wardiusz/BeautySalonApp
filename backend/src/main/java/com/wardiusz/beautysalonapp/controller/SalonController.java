package com.wardiusz.beautysalonapp.controller;

import com.wardiusz.beautysalonapp.dto.salon.SalonDto;
import com.wardiusz.beautysalonapp.dto.salon.SalonFilter;
import com.wardiusz.beautysalonapp.service.SalonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/salons")
public class SalonController {

    private final SalonService salonService;

    // GET /api/v1/salons
    // GET /api/v1/salons?district=district&services=service1,service2
    @GetMapping
    public ResponseEntity<List<SalonDto>> getSalons(@ModelAttribute SalonFilter filter) {
        return ResponseEntity.ok(salonService.getSalons(filter));
    }

    // PUT /api/salons/{id}/update
    @PutMapping("/{id}/update")
    public ResponseEntity<SalonDto> updateSalon(@PathVariable Long id, @RequestBody @Valid SalonDto dto) {
        return ResponseEntity.ok(salonService.updateSalon(id, dto));
    }
}
