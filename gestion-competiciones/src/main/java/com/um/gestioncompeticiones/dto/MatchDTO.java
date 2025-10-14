package com.um.gestioncompeticiones.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDTO {
    private Long id;
    private Long competitionId;
    private Long team1Id;
    private String team1Name;
    private Long team2Id;
    private String team2Name;
    private LocalDate matchDate;
    private int courtNumber;
}