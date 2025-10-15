package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.dto.TeamCreateDTO;
import com.um.gestioncompeticiones.exception.team.TeamAlreadyRegisteredException;
import com.um.gestioncompeticiones.exception.team.TeamNotFoundException;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.repository.CompetitionRepository;
import com.um.gestioncompeticiones.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;
    private Competition competition;

    private static final Long TEAM_ID = 1L;
    private static final Long TEAM_ID_2 = 2L;
    private static final Long COMPETITION_ID = 1L;
    private static final Long NON_EXISTENT_TEAM_ID = 99L;
    private static final Long NON_EXISTENT_COMPETITION_ID = 99L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        team = Team.builder()
                .id(TEAM_ID)
                .name("Equipo A")
                .competitions(new HashSet<>())
                .build();

        competition = Competition.builder()
                .id(COMPETITION_ID)
                .name("Liga Primavera")
                .teams(new HashSet<>())
                .build();
    }

    // -------------------------------------------------
    // Test: Crear equipo correctamente
    // -------------------------------------------------
    @Test
    void createTeamOk() {
        TeamCreateDTO dto = TeamCreateDTO.builder().name("Equipo A").build();
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        Team result = teamService.createTeam(dto);

        assertNotNull(result);
        assertEquals("Equipo A", result.getName());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    // -------------------------------------------------
    // Test: Registrar equipo a competición correctamente
    // -------------------------------------------------
    @Test
    void registerTeamToCompetitionOk() {
        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(competitionRepository.findById(COMPETITION_ID)).thenReturn(Optional.of(competition));
        when(teamRepository.existsByCompetition(TEAM_ID, COMPETITION_ID)).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        Team result = teamService.registerTeamToCompetition(TEAM_ID, COMPETITION_ID);

        assertNotNull(result);
        assertTrue(result.getCompetitions().contains(competition));
        assertTrue(competition.getTeams().contains(team));

        verify(teamRepository, times(1)).findById(TEAM_ID);
        verify(competitionRepository, times(1)).findById(COMPETITION_ID);
        verify(teamRepository, times(1)).save(any(Team.class));
        verify(competitionRepository, times(1)).save(any(Competition.class));
    }

    // -------------------------------------------------
    // Test: Excepción si el equipo ya está registrado
    // -------------------------------------------------
    @Test
    void registerTeamToCompetitionFailedAlreadyRegistered() {
        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(competitionRepository.findById(COMPETITION_ID)).thenReturn(Optional.of(competition));
        when(teamRepository.existsByCompetition(TEAM_ID, COMPETITION_ID)).thenReturn(true);

        assertThrows(TeamAlreadyRegisteredException.class,
                () -> teamService.registerTeamToCompetition(TEAM_ID, COMPETITION_ID));

        verify(teamRepository, times(1)).findById(TEAM_ID);
        verify(competitionRepository, times(1)).findById(COMPETITION_ID);
        verify(teamRepository, never()).save(any(Team.class));
        verify(competitionRepository, never()).save(any(Competition.class));
    }

    // -------------------------------------------------
    // Test: Excepción si el equipo no existe al registrar
    // -------------------------------------------------
    @Test
    void registerTeamToCompetitionFailedTeamNotFound() {
        when(teamRepository.findById(NON_EXISTENT_TEAM_ID)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class,
                () -> teamService.registerTeamToCompetition(NON_EXISTENT_TEAM_ID, COMPETITION_ID));

        verify(teamRepository, times(1)).findById(NON_EXISTENT_TEAM_ID);
        verify(competitionRepository, never()).findById(anyLong());
    }

    // -------------------------------------------------
    // Test: Excepción si la competición no existe al registrar
    // -------------------------------------------------
    @Test
    void registerTeamToCompetitionFailedCompetitionNotFound() {
        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(competitionRepository.findById(NON_EXISTENT_COMPETITION_ID)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class,
                () -> teamService.registerTeamToCompetition(TEAM_ID, NON_EXISTENT_COMPETITION_ID));

        verify(teamRepository, times(1)).findById(TEAM_ID);
        verify(competitionRepository, times(1)).findById(NON_EXISTENT_COMPETITION_ID);
        verify(teamRepository, never()).save(any(Team.class));
        verify(competitionRepository, never()).save(any(Competition.class));
    }

    // -------------------------------------------------
    // Test: Obtener todos los equipos
    // -------------------------------------------------
    @Test
    void getAllTeamsOk() {
        Team team2 = Team.builder().id(TEAM_ID_2).name("Equipo B").build();
        when(teamRepository.findAll()).thenReturn(List.of(team, team2));

        List<Team> result = teamService.getAllTeams();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(team));
        assertTrue(result.contains(team2));
        verify(teamRepository, times(1)).findAll();
    }

    // -------------------------------------------------
    // Test: Obtener todos los equipos sin equipos
    // -------------------------------------------------
    @Test
    void getAllTeamsEmpty() {
        when(teamRepository.findAll()).thenReturn(List.of());

        List<Team> result = teamService.getAllTeams();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(teamRepository, times(1)).findAll();
    }

    // -------------------------------------------------
    // Test: Obtener equipo por ID correctamente
    // -------------------------------------------------
    @Test
    void getTeamByIdOk() {
        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));

        Team result = teamService.getTeamById(TEAM_ID);

        assertNotNull(result);
        assertEquals(TEAM_ID, result.getId());
        assertEquals("Equipo A", result.getName());
        verify(teamRepository, times(1)).findById(TEAM_ID);
    }

    // -------------------------------------------------
    // Test: Excepción al obtener equipo por ID inexistente
    // -------------------------------------------------
    @Test
    void getTeamByIdFailedNotFound() {
        when(teamRepository.findById(NON_EXISTENT_TEAM_ID)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class,
                () -> teamService.getTeamById(NON_EXISTENT_TEAM_ID));

        verify(teamRepository, times(1)).findById(NON_EXISTENT_TEAM_ID);
    }
}
