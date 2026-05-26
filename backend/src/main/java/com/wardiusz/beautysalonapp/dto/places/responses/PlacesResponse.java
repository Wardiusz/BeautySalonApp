package com.wardiusz.beautysalonapp.dto.places.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wardiusz.beautysalonexplorerapp.dto.places.PlacesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesResponse {

    private List<PlacesDto> places;

    private String nextPageToken;

}
