package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.dto.CompetitionCreateDTO;
import com.um.gestioncompeticiones.exception.competition.CompetitionAlreadyExistsException;
import com.um.gestioncompeticiones.exception.competition.CompetitionNotFoundException;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;

    @Override
    public Competition createCompetition(CompetitionCreateDTO competitionCreateDTO) {
        if (competitionRepository.existsByName(competitionCreateDTO.getName())) {
            throw new CompetitionAlreadyExistsException(
                    "A competition with the name '" + competitionCreateDTO.getName() + "' already exists."
            );
        }
        Competition competition = Competition.builder()
                .name(competitionCreateDTO.getName())
                .sport(competitionCreateDTO.getSport())
                .startDate(competitionCreateDTO.getStartDate())
                .endDate(competitionCreateDTO.getEndDate())
                .numberOfCourts(competitionCreateDTO.getNumberOfCourts())
                .build();

        return competitionRepository.save(competition);
    }

    @Override
    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAll();
    }

    @Override
    public Competition getCompetitionById(Long id) {
        return competitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(
                        "Competition with id " + id + " not found."
                ));
    }

    @Override
    public List<Team> getTeamsByCompetition(Long id) {
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(
                        "Competition with id " + id + " not found."
                ));

        return new ArrayList<>(competition.getTeams());
    }
}
