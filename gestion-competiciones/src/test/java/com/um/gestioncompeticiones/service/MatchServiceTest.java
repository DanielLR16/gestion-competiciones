package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.exception.match.MatchGenerationException;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Match;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.repository.CompetitionRepository;
import com.um.gestioncompeticiones.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MatchServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Competition competition;
    private Team team1;
    private Team team2;
    private Team team3;

    private static final Long COMPETITION_ID = 1L;
    private static final Long COMPETITION_ID_COURT_LIMIT = 2L;
    private static final Long TEAM_ID = 1L;
    private static final Long TEAM_ID_2 = 2L;
    private static final Long TEAM_ID_3 = 3L;
    private static final Long NON_EXISTENT_COMPETITION_ID = 99L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        team1 = Team.builder().id(TEAM_ID).name("Equipo A").build();
        team2 = Team.builder().id(TEAM_ID_2).name("Equipo B").build();
        team3 = Team.builder().id(TEAM_ID_3).name("Equipo C").build();

        competition = Competition.builder()
                .id(COMPETITION_ID)
                .name("Liga Primavera")
                .sport("Fútbol")
                .startDate(LocalDate.of(2025, 10, 20))
                .endDate(LocalDate.of(2025, 10, 25))
                .numberOfCourts(1)
                .teams(new HashSet<>(Set.of(team1, team2, team3)))
                .matches(new ArrayList<>())
                .build();
    }

    // -------------------------------------------------
    // Test: Generar primera jornada correctamente
    // -------------------------------------------------
    @Test
    void generateFirstRoundOk() {
        when(competitionRepository.findById(COMPETITION_ID)).thenReturn(Optional.of(competition));
        when(matchRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        List<Match> matches = matchService.generateFirstRound(COMPETITION_ID);

        assertNotNull(matches);
        assertEquals(1, matches.size());
        assertEquals(competition, matches.get(0).getCompetition());
        assertTrue(matches.get(0).getTeam1() == team1 || matches.get(0).getTeam1() == team2);
        verify(matchRepository, times(1)).saveAll(matches);
    }

    // -------------------------------------------------
    // Test: Generar primera jornada falla por competición inexistente
    // -------------------------------------------------
    @Test
    void generateFirstRoundFailedCompetitionNotFound() {
        when(competitionRepository.findById(NON_EXISTENT_COMPETITION_ID)).thenReturn(Optional.empty());

        assertThrows(MatchGenerationException.class,
                () -> matchService.generateFirstRound(NON_EXISTENT_COMPETITION_ID));

        verify(matchRepository, never()).saveAll(anyList());
    }

    // -------------------------------------------------
    // Test: Generar primera jornada falla por equipos insuficientes
    // -------------------------------------------------
    @Test
    void generateFirstRoundFailedNotEnoughTeams() {
        competition.setTeams(Set.of(team1));

        when(competitionRepository.findById(COMPETITION_ID)).thenReturn(Optional.of(competition));

        assertThrows(MatchGenerationException.class,
                () -> matchService.generateFirstRound(COMPETITION_ID));

        verify(competitionRepository, times(1)).findById(COMPETITION_ID);
    }

    // -------------------------------------------------
    // Test: Generar primera jornada respetando límite de pistas por día
    // -------------------------------------------------
    @Test
    void generateFirstRoundRespectCourtLimit() {
        Competition competitionCourtLimit = Competition.builder()
                .id(COMPETITION_ID_COURT_LIMIT)
                .name("Liga Test")
                .numberOfCourts(2)
                .startDate(LocalDate.of(2025, 10, 20))
                .endDate(LocalDate.of(2025, 10, 25))
                .teams(Set.of(
                        Team.builder().id(10L).name("Equipo 1").build(),
                        Team.builder().id(20L).name("Equipo 2").build(),
                        Team.builder().id(30L).name("Equipo 3").build(),
                        Team.builder().id(40L).name("Equipo 4").build(),
                        Team.builder().id(50L).name("Equipo 5").build(),
                        Team.builder().id(60L).name("Equipo 6").build(),
                        Team.builder().id(70L).name("Equipo 7").build(),
                        Team.builder().id(80L).name("Equipo 8").build(),
                        Team.builder().id(90L).name("Equipo 9").build(),
                        Team.builder().id(100L).name("Equipo 10").build()
                ))
                .build();
        when(competitionRepository.findById(COMPETITION_ID_COURT_LIMIT)).thenReturn(Optional.of(competitionCourtLimit));

        List<Match> matches = matchService.generateFirstRound(COMPETITION_ID_COURT_LIMIT);

        int expectedMaxMatches = competitionCourtLimit.getNumberOfCourts() * MatchServiceImpl.MAX_MATCHES_PER_COURT_PER_DAY;

        assertNotNull(matches);
        assertTrue(matches.size() <= expectedMaxMatches,
                "Se han generado más partidos de los permitidos por día");

        matches.forEach(match -> {
            assertTrue(match.getCourtNumber() >= 1 && match.getCourtNumber() <= competitionCourtLimit.getNumberOfCourts(),
                    "Número de pista fuera de rango");
            assertEquals(competitionCourtLimit.getStartDate(), match.getMatchDate(), "Fecha del partido incorrecta");
        });

        verify(competitionRepository, times(1)).findById(COMPETITION_ID_COURT_LIMIT);
        verify(matchRepository, times(1)).saveAll(matches);
    }

    // -------------------------------------------------
    // Test: Obtener partidos de una competición
    // -------------------------------------------------
    @Test
    void getMatchesByCompetitionOk() {
        Match match = Match.builder().competition(competition).team1(team1).team2(team2).build();
        when(matchRepository.findByCompetitionId(COMPETITION_ID)).thenReturn(List.of(match));

        List<Match> result = matchService.getMatchesByCompetition(COMPETITION_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(competition, result.get(0).getCompetition());
        verify(matchRepository, times(1)).findByCompetitionId(COMPETITION_ID);
    }

    // -------------------------------------------------
    // Test: Obtener partidos de una competición sin partidos
    // -------------------------------------------------
    @Test
    void getMatchesByCompetitionEmpty() {
        when(matchRepository.findByCompetitionId(COMPETITION_ID)).thenReturn(List.of());

        List<Match> result = matchService.getMatchesByCompetition(COMPETITION_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(matchRepository, times(1)).findByCompetitionId(COMPETITION_ID);
    }

    // -------------------------------------------------
    // Test: Obtener equipos no asignados a partidos
    // -------------------------------------------------
    @Test
    void getUnassignedTeamsOk() {
        Match match = Match.builder().competition(competition).team1(team1).team2(team2).build();
        competition.getMatches().add(match);

        when(competitionRepository.findById(COMPETITION_ID)).thenReturn(Optional.of(competition));

        List<Team> unassignedTeams = matchService.getUnassignedTeams(COMPETITION_ID);

        assertNotNull(unassignedTeams);
        assertEquals(1, unassignedTeams.size());
        assertTrue(unassignedTeams.contains(team3));
        verify(competitionRepository, times(1)).findById(COMPETITION_ID);
    }

    // -------------------------------------------------
    // Test: Obtener equipos no asignados a partidos pero sin equipos
    // -------------------------------------------------
    @Test
    void getUnassignedTeamsEmpty() {
        Match match = Match.builder().competition(competition).team1(team1).team2(team2).build();
        competition.getMatches().add(match);
        competition.setTeams(Set.of(team1, team2));

        when(competitionRepository.findById(COMPETITION_ID)).thenReturn(Optional.of(competition));

        List<Team> unassignedTeams = matchService.getUnassignedTeams(COMPETITION_ID);

        assertNotNull(unassignedTeams);
        assertTrue(unassignedTeams.isEmpty());
        verify(competitionRepository, times(1)).findById(COMPETITION_ID);
    }

    // -------------------------------------------------
    // Test: Equipos no asignados falla por competición inexistente
    // -------------------------------------------------
    @Test
    void getUnassignedTeamsCompetitionNotFound() {
        when(competitionRepository.findById(NON_EXISTENT_COMPETITION_ID)).thenReturn(Optional.empty());

        assertThrows(MatchGenerationException.class,
                () -> matchService.getUnassignedTeams(NON_EXISTENT_COMPETITION_ID));

        verify(competitionRepository, times(1)).findById(NON_EXISTENT_COMPETITION_ID);
    }

}
