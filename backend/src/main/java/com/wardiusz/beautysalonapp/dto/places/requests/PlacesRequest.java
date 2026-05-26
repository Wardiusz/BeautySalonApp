package com.wardiusz.beautysalonapp.dto.places.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlacesRequest {

    private String textQuery;

    private int maxResultCount;

    private String languageCode;

    private String pageToken;

}

