package com.wardiusz.beautysalonapp.dto.places;

import com.wardiusz.beautysalonapp.repository.SalonRepository;
import com.wardiusz.beautysalonapp.service.PlacesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
@Profile("seed")
public class PlacesDataSeeder implements ApplicationRunner {

    private final PlacesService placesService;
    private final SalonRepository salonRepository;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        if (salonRepository.count() > 0) {
            log.info("Database already seeded — skipping.");
            return;
        }

        int saved = placesService.seedSalons();
        log.info("Seed complete. Imported {} salons.", saved);
    }
}