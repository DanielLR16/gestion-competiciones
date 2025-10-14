package com.um.gestioncompeticiones.controller;

import com.um.gestioncompeticiones.dto.*;
import com.um.gestioncompeticiones.mapper.DtoMapper;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Match;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.service.CompetitionService;
import com.um.gestioncompeticiones.service.MatchService;
import com.um.gestioncompeticiones.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;
    private final TeamService teamService;
    private final MatchService matchService;
    private final DtoMapper mapper;

    // -------------------------------
    // Crear nueva competición
    // -------------------------------
    @PostMapping
    public ResponseEntity<CompetitionDTO> createCompetition(@RequestBody @Valid CompetitionCreateDTO competitionCreateDTO) {
        Competition competition = competitionService.createCompetition(competitionCreateDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toCompetitionDTO(competition));
    }

    // -------------------------------
    // Listar todas las competiciones
    // -------------------------------
    @GetMapping
    public ResponseEntity<List<CompetitionDTO>> getAllCompetitions() {
        List<Competition> competitions = competitionService.getAllCompetitions();
        return ResponseEntity.ok(mapper.toCompetitionDTOList(competitions));
    }

    // -------------------------------
    // Obtener una competición por id
    // -------------------------------
    @GetMapping("/{competitionId}")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long competitionId) {
        Competition competition = competitionService.getCompetitionById(competitionId);
        return ResponseEntity.ok(mapper.toCompetitionDTO(competition));
    }

    // -------------------------------
    // Consultar equipos de una competición
    // -------------------------------
    @GetMapping("/{competitionId}/teams")
    public ResponseEntity<List<TeamDTO>> getTeamsByCompetition(@PathVariable Long competitionId) {
        List<Team> teams = competitionService.getTeamsByCompetition(competitionId);
        return ResponseEntity.ok(mapper.toTeamDTOList(teams));
    }


    // -----------------------------------------
    // Registrar equipo en una competición
    // -----------------------------------------
    @PostMapping("/{competitionId}/teams")
    public ResponseEntity<TeamDTO> registerTeamInCompetition(
            @PathVariable Long competitionId,
            @RequestParam Long teamId) {

        Team team = teamService.registerTeamToCompetition(competitionId, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toTeamDTO(team));
    }


    // --------------------------------------------------------
    // Generar partidos de la primera jornada (first round)
    // --------------------------------------------------------
    @PostMapping("/{competitionId}/matches/first-round")
    public ResponseEntity<List<MatchDTO>> generateFirstRound(@PathVariable Long competitionId) {
        List<Match> matches = matchService.generateFirstRound(competitionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toMatchDTOList(matches));
    }

    // ----------------------------------------------
    // Consultar partidos de la primera jornada
    // ----------------------------------------------
    @GetMapping("/{competitionId}/matches")
    public ResponseEntity<List<MatchDTO>> getMatches(@PathVariable Long competitionId) {
        List<Match> matches = matchService.getMatchesByCompetition(competitionId);
        return ResponseEntity.ok(mapper.toMatchDTOList(matches));
    }

    // --------------------------------------------------------
    // Consultar equipos no asignados a partidos (restantes)
    // --------------------------------------------------------
    @GetMapping("/{competitionId}/matches/unassigned-teams")
    public ResponseEntity<List<TeamDTO>> getUnassignedTeams(@PathVariable Long competitionId) {
        List<Team> unassignedTeams = matchService.getUnassignedTeams(competitionId);
        return ResponseEntity.ok(mapper.toTeamDTOList(unassignedTeams));
    }
}
