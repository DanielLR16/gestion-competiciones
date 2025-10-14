package com.um.gestioncompeticiones.controller;

import com.um.gestioncompeticiones.dto.TeamCreateDTO;
import com.um.gestioncompeticiones.dto.TeamDTO;
import com.um.gestioncompeticiones.mapper.DtoMapper;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.service.TeamService;

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
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamCreateDTO teamCreateDTO) {
        Team team = teamService.createTeam(teamCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toTeamDTO(team));
    }

    // -------------------------------
    // Listar todos los equipos
    // -------------------------------
    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = mapper.toTeamDTOList(teamService.getAllTeams());
        return ResponseEntity.ok(teams);
    }

    // -------------------------------
    // Obtener equipo por id
    // -------------------------------
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(mapper.toTeamDTO(team));
    }

}
