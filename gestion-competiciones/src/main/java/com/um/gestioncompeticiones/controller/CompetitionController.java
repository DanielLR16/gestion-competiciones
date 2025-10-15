package com.um.gestioncompeticiones.controller;

import com.um.gestioncompeticiones.dto.*;
import com.um.gestioncompeticiones.mapper.DtoMapper;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Match;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.service.CompetitionService;
import com.um.gestioncompeticiones.service.MatchService;
import com.um.gestioncompeticiones.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Crear una nueva competición", description = "Permite crear una nueva competición con los datos proporcionados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Competición creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o formato incorrecto"),
            @ApiResponse(responseCode = "409", description = "Nombre de competición ya existente")
    })
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
    @Operation(summary = "Listar todas las competiciones", description = "Devuelve todas las competiciones existentes en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de competiciones devuelta correctamente")
    })
    public ResponseEntity<List<CompetitionDTO>> getAllCompetitions() {
        List<Competition> competitions = competitionService.getAllCompetitions();
        return ResponseEntity.ok(mapper.toCompetitionDTOList(competitions));
    }

    // -------------------------------
    // Obtener una competición por id
    // -------------------------------
    @GetMapping("/{competitionId}")
    @Operation(summary = "Obtener competición por ID", description = "Devuelve los datos de una competición específica dado su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Competición encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró la competición con el ID proporcionado")
    })
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long competitionId) {
        Competition competition = competitionService.getCompetitionById(competitionId);
        return ResponseEntity.ok(mapper.toCompetitionDTO(competition));
    }

    // -------------------------------
    // Consultar equipos de una competición
    // -------------------------------
    @GetMapping("/{competitionId}/teams")
    @Operation(summary = "Listar equipos de una competición", description = "Devuelve todos los equipos registrados en una competición específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de equipos devuelta correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la competición con el ID proporcionado")
    })
    public ResponseEntity<List<TeamDTO>> getTeamsByCompetition(@PathVariable Long competitionId) {
        List<Team> teams = competitionService.getTeamsByCompetition(competitionId);
        return ResponseEntity.ok(mapper.toTeamDTOList(teams));
    }


    // -----------------------------------------
    // Registrar equipo en una competición
    // -----------------------------------------
    @PostMapping("/{competitionId}/teams")
    @Operation(summary = "Registrar equipo en competición", description = "Registra un equipo en la competición especificada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipo registrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Equipo o competición no encontrados"),
            @ApiResponse(responseCode = "409", description = "El equipo ya está registrado en la competición")
    })
    public ResponseEntity<TeamDTO> registerTeamInCompetition(
            @PathVariable Long competitionId,
            @RequestParam Long teamId) {

        Team team = teamService.registerTeamToCompetition(teamId, competitionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toTeamDTO(team));
    }


    // --------------------------------------------------------
    // Generar partidos de la primera jornada (first round)
    // --------------------------------------------------------
    @PostMapping("/{competitionId}/matches/first-round")
    @Operation(summary = "Generar primera jornada", description = "Genera automáticamente los partidos de la primera jornada para la competición indicada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Partidos generados correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la competición"),
            @ApiResponse(responseCode = "400", description = "No hay suficientes equipos para generar partidos")
    })
    public ResponseEntity<List<MatchDTO>> generateFirstRound(@PathVariable Long competitionId) {
        List<Match> matches = matchService.generateFirstRound(competitionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toMatchDTOList(matches));
    }

    // ----------------------------------------------
    // Consultar partidos de la primera jornada
    // ----------------------------------------------
    @GetMapping("/{competitionId}/matches")
    @Operation(summary = "Listar partidos de la primera jornada", description = "Devuelve todos los partidos de la primera jornada de la competición indicada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de partidos devuelta correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la competición")
    })
    public ResponseEntity<List<MatchDTO>> getMatches(@PathVariable Long competitionId) {
        List<Match> matches = matchService.getMatchesByCompetition(competitionId);
        return ResponseEntity.ok(mapper.toMatchDTOList(matches));
    }

    // --------------------------------------------------------
    // Consultar equipos no asignados a partidos (restantes)
    // --------------------------------------------------------
    @GetMapping("/{competitionId}/matches/unassigned-teams")
    @Operation(summary = "Equipos no asignados a partidos", description = "Devuelve los equipos de la competición que aún no han sido asignados a ningún partido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de equipos no asignados devuelta correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la competición")
    })
    public ResponseEntity<List<TeamDTO>> getUnassignedTeams(@PathVariable Long competitionId) {
        List<Team> unassignedTeams = matchService.getUnassignedTeams(competitionId);
        return ResponseEntity.ok(mapper.toTeamDTOList(unassignedTeams));
    }
}
