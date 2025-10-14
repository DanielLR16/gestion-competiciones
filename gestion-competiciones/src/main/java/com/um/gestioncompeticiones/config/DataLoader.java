package com.um.gestioncompeticiones.config;

import com.um.gestioncompeticiones.dto.CompetitionCreateDTO;
import com.um.gestioncompeticiones.dto.TeamCreateDTO;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Team;

import com.um.gestioncompeticiones.service.CompetitionService;
import com.um.gestioncompeticiones.service.TeamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    @Transactional
    CommandLineRunner loadData(CompetitionService competitionService, TeamService teamService) {
        return args -> {
            // -------------------------------
            // Crear competiciones de prueba
            // -------------------------------
            Competition comp1 = competitionService.createCompetition(
                    CompetitionCreateDTO.builder()
                            .name("Torneo Primavera")
                            .sport("Tenis")
                            .startDate(LocalDate.now().plusDays(1))
                            .endDate(LocalDate.now().plusDays(7))
                            .numberOfCourts(2)
                            .build()
            );

            Competition comp2 = competitionService.createCompetition(
                    CompetitionCreateDTO.builder()
                            .name("Copa Verano")
                            .sport("Fútbol")
                            .startDate(LocalDate.now().plusDays(10))
                            .endDate(LocalDate.now().plusDays(17))
                            .numberOfCourts(3)
                            .build()
            );

            // -------------------------------
            // Crear equipos de prueba
            // -------------------------------
            Team teamA = teamService.createTeam(
                    TeamCreateDTO.builder().name("Team A").build()
            );
            Team teamB = teamService.createTeam(
                    TeamCreateDTO.builder().name("Team B").build()
            );
            Team teamC = teamService.createTeam(
                    TeamCreateDTO.builder().name("Team C").build()
            );
            Team teamD = teamService.createTeam(
                    TeamCreateDTO.builder().name("Team D").build()
            );

            // -------------------------------
            // Registrar equipos en competiciones
            // -------------------------------
            try {
                // Torneo Primavera
                teamService.registerTeamToCompetition(teamA.getId(), comp1.getId());
                teamService.registerTeamToCompetition(teamB.getId(), comp1.getId());
                teamService.registerTeamToCompetition(teamC.getId(), comp1.getId());

                // Copa Verano
                teamService.registerTeamToCompetition(teamB.getId(), comp2.getId());
                teamService.registerTeamToCompetition(teamC.getId(), comp2.getId());
                teamService.registerTeamToCompetition(teamD.getId(), comp2.getId());
            } catch (Exception e) {
                System.err.println("Error registrando equipos en competiciones: " + e.getMessage());
            }

            System.out.println("Datos de prueba cargados correctamente.");
        };
    }
}