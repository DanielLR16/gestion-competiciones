package com.um.gestioncompeticiones.mapper;

import com.um.gestioncompeticiones.dto.CompetitionDTO;
import com.um.gestioncompeticiones.dto.MatchDTO;
import com.um.gestioncompeticiones.dto.TeamDTO;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Match;
import com.um.gestioncompeticiones.model.Team;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    // --- Competition ---
    public CompetitionDTO toCompetitionDTO(Competition competition) {
        return CompetitionDTO.builder()
                .id(competition.getId())
                .name(competition.getName())
                .sport(competition.getSport())
                .startDate(competition.getStartDate())
                .endDate(competition.getEndDate())
                .numberOfCourts(competition.getNumberOfCourts())
                .build();
    }

    public List<CompetitionDTO> toCompetitionDTOList(List<Competition> competitions) {
        return competitions.stream().map(this::toCompetitionDTO).collect(Collectors.toList());
    }

    // --- Team ---
    public TeamDTO toTeamDTO(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }

    public List<TeamDTO> toTeamDTOList(List<Team> teams) {
        return teams.stream().map(this::toTeamDTO).collect(Collectors.toList());
    }

    // --- Match ---
    public MatchDTO toMatchDTO(Match match) {
        return MatchDTO.builder()
                .id(match.getId())
                .competitionId(match.getCompetition().getId())
                .team1Id(match.getTeam1().getId())
                .team1Name(match.getTeam1().getName())
                .team2Id(match.getTeam2().getId())
                .team2Name(match.getTeam2().getName())
                .matchDate(match.getMatchDate())
                .courtNumber(match.getCourtNumber())
                .build();
    }

    public List<MatchDTO> toMatchDTOList(List<Match> matches) {
        return matches.stream().map(this::toMatchDTO).collect(Collectors.toList());
    }
}
