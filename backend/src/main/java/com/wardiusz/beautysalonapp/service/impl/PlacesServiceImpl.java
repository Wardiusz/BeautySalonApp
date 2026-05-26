package com.wardiusz.beautysalonapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wardiusz.beautysalonapp.dto.places.PlacesDto;
import com.wardiusz.beautysalonapp.dto.places.requests.PlacesRequest;
import com.wardiusz.beautysalonapp.dto.places.responses.PlacesResponse;
import com.wardiusz.beautysalonapp.entity.Salon;
import com.wardiusz.beautysalonapp.mapper.PlacesMapper;
import com.wardiusz.beautysalonapp.repository.SalonRepository;
import com.wardiusz.beautysalonapp.service.PlacesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PlacesServiceImpl implements PlacesService {

    private static final String SEARCH_URL =
            "https://places.googleapis.com/v1/places:searchText";

    private static final String FIELD_MASK =
            """
                places.id,
                places.displayName.text,
                places.formattedAddress,
                places.nationalPhoneNumber,
                places.websiteUri,
                places.rating,
                places.userRatingCount,
                places.priceLevel,
                places.types,
                places.addressComponents,
                nextPageToken
            """.replaceAll("\\s+", "");

    private static final List<String> DISTRICTS = List.of(
            "Śródmieście Warszawa",
            "Mokotów Warszawa",
            "Wola Warszawa",
            "Praga-Południe Warszawa",
            "Ursynów Warszawa",
            "Bemowo Warszawa",
            "Targówek Warszawa",
            "Białołęka Warszawa",
            "Żoliborz Warszawa",
            "Ochota Warszawa"
    );

    private static final List<String> SEARCH_TERMS = List.of(
            "salon fryzjerski",
            "salon kosmetyczny",
            "hair salon",
            "beauty salon"
    );

    @Value("${google.places.api-key}")
    private String apiKey;

    @Value("${google.places.target-count:100}")
    private int targetCount;

    private final RestTemplate restTemplate;
    private final SalonRepository salonRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlacesServiceImpl(RestTemplate restTemplate, SalonRepository salonRepository) {
        this.restTemplate = restTemplate;
        this.salonRepository = salonRepository;
    }

    @Override
    public int seedSalons() {
        Map<String, Salon> uniqueSalons = new LinkedHashMap<>();

        outer:
        for (String term : SEARCH_TERMS) {
            for (String district : DISTRICTS) {
                if (uniqueSalons.size() >= targetCount) break outer;

                String query = term + " " + district;
                log.info("Fetching: '{}'", query);
                fetchAllPages(query, uniqueSalons);
            }
        }

        log.info("Unique salons collected before save: {}", uniqueSalons.size());

        List<Salon> toSave = new ArrayList<>(uniqueSalons.values())
                .subList(0, Math.min(uniqueSalons.size(), targetCount));

        salonRepository.saveAll(toSave);
        log.info("Saved {} salons to the database.", toSave.size());

        return toSave.size();
    }

    private void fetchAllPages(String query, Map<String, Salon> accumulator) {
        String nextPageToken = null;
        int page = 0;

        do {
            PlacesResponse response = callApi(query, nextPageToken);
            if (response == null) {
                log.warn("!!  Got null response for query '{}'", query);
                break;
            }
            if (response.getPlaces() == null || response.getPlaces().isEmpty()) {
                log.warn("!!  Empty/null places list for query '{}'", query);
                break;
            }

            log.info("  → {} places returned", response.getPlaces().size());

            for (PlacesDto place : response.getPlaces()) {
                log.info("  Place id={} displayName={}", place.getId(), place.getDisplayName());

                if (!accumulator.containsKey(place.getId())) {
                    Salon salon = PlacesMapper.toSalon(place);
                    if (salon != null) {
                        accumulator.put(place.getId(), salon);
                    } else {
                        log.warn("!! Mapper returned null for id={}", place.getId());
                    }
                }
            }

            nextPageToken = response.getNextPageToken();
            page++;
            if (nextPageToken != null) sleep(2000);

        } while (nextPageToken != null && page < 3);
    }

    private PlacesResponse callApi(String query, String pageToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", apiKey);
        headers.set("X-Goog-FieldMask", FIELD_MASK);

        PlacesRequest requestBody = PlacesRequest.builder()
                .textQuery(query)
                .maxResultCount(20)
                .languageCode("pl")
                .pageToken(pageToken)
                .build();

        HttpEntity<PlacesRequest> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> raw = restTemplate.exchange(
                    SEARCH_URL, HttpMethod.POST, request, String.class);

            String body = raw.getBody();

            if (raw.getStatusCode() != HttpStatus.OK || body == null) {
                log.warn("  Non-OK status {}", raw.getStatusCode());
                return null;
            }

            return objectMapper.readValue(body, PlacesResponse.class);

        } catch (Exception e) {
            log.error("!!  API call failed: {}", e.getMessage(), e);
        }
        return null;
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}