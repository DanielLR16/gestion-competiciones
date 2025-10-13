package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.model.Team;

import java.util.List;

public interface TeamService {

    Team createTeam(Team team);

    Team registerTeamToCompetition(Long teamId, Long competitionId);

    List<Team> getAllTeams();

    Team getTeamById(Long id);

    Team getTeamByName(String name);
}
