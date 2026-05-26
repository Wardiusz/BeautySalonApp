package com.wardiusz.beautysalonapp.dto.salon;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SalonFilter {

    private String district;

    private List<String> services;

}
