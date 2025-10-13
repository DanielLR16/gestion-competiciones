package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.exception.competition.CompetitionAlreadyExistsException;
import com.um.gestioncompeticiones.exception.competition.CompetitionNotFoundException;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;

    @Override
    public Competition createCompetition(Competition competition) {
        if (competitionRepository.existsByName(competition.getName())) {
            throw new CompetitionAlreadyExistsException(
                    "A competition with the name '" + competition.getName() + "' already exists."
            );
        }
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
    public Competition getCompetitionByName(String name) {
        return competitionRepository.findByName(name)
                .orElseThrow(() -> new CompetitionNotFoundException(
                        "Competition with name '" + name + "' not found."
                ));
    }
}
