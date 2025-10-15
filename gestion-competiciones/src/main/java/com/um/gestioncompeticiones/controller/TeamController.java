package com.um.gestioncompeticiones.controller;

import com.um.gestioncompeticiones.dto.TeamCreateDTO;
import com.um.gestioncompeticiones.dto.TeamDTO;
import com.um.gestioncompeticiones.mapper.DtoMapper;
import com.um.gestioncompeticiones.model.Team;
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
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final DtoMapper mapper;

    // -------------------------------
    // Crear nuevo equipo
    // -------------------------------
    @PostMapping
    @Operation(summary = "Crear un nuevo equipo", description = "Crea un nuevo equipo con la información proporcionada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes")
    })
    public ResponseEntity<TeamDTO> createTeam(@RequestBody @Valid TeamCreateDTO teamCreateDTO) {
        Team team = teamService.createTeam(teamCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toTeamDTO(team));
    }

    // -------------------------------
    // Listar todos los equipos
    // -------------------------------
    @GetMapping
    @Operation(summary = "Listar todos los equipos", description = "Obtiene la lista de todos los equipos registrados en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de equipos obtenida correctamente")
    })
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = mapper.toTeamDTOList(teamService.getAllTeams());
        return ResponseEntity.ok(teams);
    }

    // -------------------------------
    // Obtener equipo por id
    // -------------------------------
    @GetMapping("/{teamId}")
    @Operation(summary = "Obtener equipo por ID", description = "Obtiene un equipo específico mediante su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipo obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(mapper.toTeamDTO(team));
    }

}
