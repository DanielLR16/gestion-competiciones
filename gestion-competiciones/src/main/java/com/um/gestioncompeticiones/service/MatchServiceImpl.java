package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.exception.match.MatchGenerationException;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Match;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.repository.CompetitionRepository;
import com.um.gestioncompeticiones.repository.MatchRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    public static final int MAX_MATCHES_PER_COURT_PER_DAY = 2;

    private final CompetitionRepository competitionRepository;
    private final MatchRepository matchRepository;

    @Override
    public List<Match> generateFirstRound(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new MatchGenerationException("Competition with id " + competitionId + " not found."));

        Set<Team> teamsSet = competition.getTeams();
        if (teamsSet.size() < 2) {
            throw new MatchGenerationException("Not enough teams to generate matches.");
        }

        List<Team> teams = new ArrayList<>(teamsSet);
        Collections.shuffle(teams); // Mezcla aleatoria para emparejamiento

        List<Match> matches = new ArrayList<>();

        assignMatches(competition, teams, matches);

        // Guardar partidos generados
        matchRepository.saveAll(matches);

        return matches;
    }

    private void assignMatches(Competition competition, List<Team> teams, List<Match> matches) {

        int totalCourts = competition.getNumberOfCourts();
        int maxMatchesPerDay = totalCourts * MAX_MATCHES_PER_COURT_PER_DAY;
        LocalDate matchDate = competition.getStartDate();
        // Genera los partidos
        List<Match> generatedMatches = IntStream.range(0, teams.size() / 2)
                .limit(maxMatchesPerDay)
                .mapToObj(i -> {
                    int teamIndex = i * 2;
                    int courtNumber = (i % totalCourts) + 1;

                    return Match.builder()
                            .competition(competition)
                            .team1(teams.get(teamIndex))
                            .team2(teams.get(teamIndex + 1))
                            .matchDate(matchDate)
                            .courtNumber(courtNumber)
                            .build();
                }).toList();

        matches.addAll(generatedMatches);
    }

    @Override
    public List<Match> getMatchesByCompetition(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new MatchGenerationException("Competition with id " + competitionId + " not found."));
        return competition.getMatches();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Team> getUnassignedTeams(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new MatchGenerationException("Competition with id " + competitionId + " not found."));

        // Equipos que ya tienen partido (usando los matches asociados a la competición)
        Set<Team> assignedTeams = competition.getMatches().stream()
                .flatMap(match -> Stream.of(match.getTeam1(), match.getTeam2()))
                .collect(Collectors.toSet());

        // Equipos no asignados = todos - asignados
        return competition.getTeams().stream()
                .filter(team -> !assignedTeams.contains(team))
                .toList();
    }
}
