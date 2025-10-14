package com.um.gestioncompeticiones.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionDTO {
    private Long id;
    private String name;
    private String sport;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfCourts;
}
