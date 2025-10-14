package com.um.gestioncompeticiones.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionCreateDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Sport is mandatory")
    private String sport;

    @NotNull(message = "Start date is mandatory")
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory")
    private LocalDate endDate;

    @Min(value = 1, message = "There must be at least one court")
    private int numberOfCourts;
}