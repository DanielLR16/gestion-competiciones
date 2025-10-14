package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.dto.CompetitionCreateDTO;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Team;

import java.util.List;

public interface CompetitionService {

    Competition createCompetition(CompetitionCreateDTO competitionCreateDTO);

    List<Competition> getAllCompetitions();

    Competition getCompetitionById(Long id);

    List<Team> getTeamsByCompetition(Long id);

}
