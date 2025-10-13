package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.exception.team.TeamAlreadyRegisteredException;
import com.um.gestioncompeticiones.exception.team.TeamNotFoundException;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Team;
import com.um.gestioncompeticiones.repository.CompetitionRepository;
import com.um.gestioncompeticiones.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final CompetitionRepository competitionRepository;

    @Override
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Team registerTeamToCompetition(Long teamId, Long competitionId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team with id " + teamId + " not found"));

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new TeamNotFoundException("Competition with id " + competitionId + " not found"));

        // Validar que no esté ya registrado
        if (teamRepository.existsByCompetition(teamId, competitionId)) {
            throw new TeamAlreadyRegisteredException(
                    "Team '" + team.getName() + "' is already registered in competition '" + competition.getName() + "'"
            );
        }


        // Registrar el equipo en la competición
        team.getCompetitions().add(competition);
        competition.getTeams().add(team);

        // Guardar ambos para persistir la relación
        competitionRepository.save(competition);
        return teamRepository.save(team);
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team with id " + id + " not found"));
    }

    @Override
    public Team getTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(() -> new TeamNotFoundException("Team with name '" + name + "' not found"));
    }
}
