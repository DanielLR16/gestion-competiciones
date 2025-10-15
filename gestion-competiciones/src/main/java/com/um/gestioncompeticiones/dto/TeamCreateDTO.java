package com.um.gestioncompeticiones.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCreateDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;
}
