package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.model.Competition;

import java.util.List;

public interface CompetitionService {

    Competition createCompetition(Competition competition);

    List<Competition> getAllCompetitions();

    Competition getCompetitionById(Long id);

    Competition getCompetitionByName(String name);

}
