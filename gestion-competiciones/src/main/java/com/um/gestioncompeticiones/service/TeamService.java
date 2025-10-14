package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.dto.TeamCreateDTO;
import com.um.gestioncompeticiones.model.Team;

import java.util.List;

public interface TeamService {

    Team createTeam(TeamCreateDTO teamCreateDTO);

    Team registerTeamToCompetition(Long teamId, Long competitionId);

    List<Team> getAllTeams();

    Team getTeamById(Long id);
}
