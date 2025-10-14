package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.model.Match;
import com.um.gestioncompeticiones.model.Team;

import java.util.List;

public interface MatchService {
    /**
     * Genera automáticamente los partidos de la primera jornada para una competición.
     *
     * @param competitionId ID de la competición
     * @return lista de partidos generados
     */
    List<Match> generateFirstRound(Long competitionId);

    /**
     * Devuelve todos los partidos de una competición
     */
    List<Match> getMatchesByCompetition(Long competitionId);

    /**
     * Devuelve los equipos no asignados a partidos en la primera jornada
     */
    List<Team> getUnassignedTeams(Long competitionId);
}
