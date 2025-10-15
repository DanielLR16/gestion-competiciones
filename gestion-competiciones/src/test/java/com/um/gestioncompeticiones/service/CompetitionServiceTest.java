package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.dto.CompetitionCreateDTO;
import com.um.gestioncompeticiones.exception.competition.CompetitionAlreadyExistsException;
import com.um.gestioncompeticiones.exception.competition.CompetitionNotFoundException;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.repository.CompetitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompetitionServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private CompetitionServiceImpl competitionService;

    private Competition competition;

    private static final Long EXISTING_COMPETITION_ID = 1L;
    private static final Long EXISTING_COMPETITION_ID_2 = 2L;
    private static final Long NON_EXISTENT_COMPETITION_ID = 99L;
    private static final Long TEAM_ID_1 = 1L;
    private static final Long TEAM_ID_2 = 2L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        competition = Competition.builder()
                .id(EXISTING_COMPETITION_ID)
                .name("Liga Primavera")
                .sport("Fútbol")
                .startDate(LocalDate.of(2025, 10, 20))
                .endDate(LocalDate.of(2025, 10, 25))
                .numberOfCourts(2)
                .build();
    }

    // -------------------------------------------------
    // Test: Crear una nueva competición
    // -------------------------------------------------
    @Test
    public void createCompetitionOk() {
        CompetitionCreateDTO competitionCreateDTO = CompetitionCreateDTO.builder()
                .name("Liga Primavera")
                .sport("Fútbol")
                .startDate(LocalDate.of(2025, 3, 1))
                .endDate(LocalDate.of(2025, 6, 30))
                .numberOfCourts(2)
                .build();

        when(competitionRepository.existsByName(competitionCreateDTO.getName())).thenReturn(false);
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        Competition result = competitionService.createCompetition(competitionCreateDTO);

        assertNotNull(result);
        assertEquals("Liga Primavera", result.getName());
        verify(competitionRepository, times(1)).save(any(Competition.class));

    }

    // -------------------------------------------------
    // Test: Exception no crear si el nombre ya existe
    // -------------------------------------------------
    @Test
    void createCompetitionFailedAlreadyExists() {
        CompetitionCreateDTO competitionCreateDTO = CompetitionCreateDTO.builder()
                .name("Liga Primavera")
                .build();

        when(competitionRepository.existsByName(competitionCreateDTO.getName())).thenReturn(true);

        assertThrows(CompetitionAlreadyExistsException.class,
                () -> competitionService.createCompetition(competitionCreateDTO));

        verify(competitionRepository, never()).save(any(Competition.class));
    }

    // -------------------------------------------------
    // Tests: Obtener todas las competiciones con competiciones existentes
    // -------------------------------------------------
    @Test
    void getAllCompetitionsOk() {
        Competition comp2 = Competition.builder().id(EXISTING_COMPETITION_ID_2).name("Copa Verano").build();

        List<Competition> competitions = List.of(competition, comp2);

        when(competitionRepository.findAll()).thenReturn(competitions);

        List<Competition> result = competitionService.getAllCompetitions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Liga Primavera", result.get(0).getName());
        assertEquals("Copa Verano", result.get(1).getName());

        verify(competitionRepository, times(1)).findAll();
    }

    // -------------------------------------------------
    // Tests: Obtener todas las competiciones sin competiciones existentes
    // -------------------------------------------------
    @Test
    void getAllCompetitionsEmpty() {
        when(competitionRepository.findAll()).thenReturn(List.of());

        List<Competition> result = competitionService.getAllCompetitions();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(competitionRepository, times(1)).findAll();
    }

    // -------------------------------------------------
    // Test: Obtener una competición por ID correctamente
    // -------------------------------------------------
    @Test
    void getCompetitionByIdOk() {
        when(competitionRepository.findById(competition.getId())).thenReturn(Optional.of(competition));

        Competition result = competitionService.getCompetitionById(competition.getId());

        assertNotNull(result);
        assertEquals(competition.getId(), result.getId());
        assertEquals(competition.getName(), result.getName());

        verify(competitionRepository, times(1)).findById(competition.getId());
    }

    // -------------------------------------------------
    // Test: Excepción si la competición no existe
    // -------------------------------------------------
    @Test
    void getCompetitionByIdFailedNotFound() {
        when(competitionRepository.findById(NON_EXISTENT_COMPETITION_ID)).thenReturn(Optional.empty());

        assertThrows(CompetitionNotFoundException.class,
                () -> competitionService.getCompetitionById(NON_EXISTENT_COMPETITION_ID));

        verify(competitionRepository, times(1)).findById(NON_EXISTENT_COMPETITION_ID);
    }

    // -------------------------------------------------
    // Test: Obtener equipos de una competición con equipos
    // -------------------------------------------------
    @Test
    void getTeamsByCompetitionOk() {
        Team team1 = Team.builder().id(TEAM_ID_1).name("Equipo A").build();
        Team team2 = Team.builder().id(TEAM_ID_2).name("Equipo B").build();
        Set<Team> teamsSet = Set.of(team1, team2);

        competition.setTeams(teamsSet);

        when(competitionRepository.findById(competition.getId())).thenReturn(Optional.of(competition));

        List<Team> result = competitionService.getTeamsByCompetition(competition.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(team1));
        assertTrue(result.contains(team2));

        verify(competitionRepository, times(1)).findById(competition.getId());
    }

    // -------------------------------------------------
    // Test: Obtener equipos de una competición sin equipos
    // -------------------------------------------------
    @Test
    void getTeamsByCompetitionEmpty() {
        competition.setTeams(Collections.emptySet());

        when(competitionRepository.findById(competition.getId())).thenReturn(Optional.of(competition));

        List<Team> result = competitionService.getTeamsByCompetition(competition.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(competitionRepository, times(1)).findById(competition.getId());
    }

    // -------------------------------------------------
    // Test: Excepción si la competición no existe
    // -------------------------------------------------
    @Test
    void getTeamsByCompetitionNotFound() {
        when(competitionRepository.findById(NON_EXISTENT_COMPETITION_ID)).thenReturn(Optional.empty());

        assertThrows(CompetitionNotFoundException.class,
                () -> competitionService.getTeamsByCompetition(NON_EXISTENT_COMPETITION_ID));

        verify(competitionRepository, times(1)).findById(NON_EXISTENT_COMPETITION_ID);
    }
}
