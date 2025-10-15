package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.model.Match;
import com.um.gestioncompeticiones.model.Team;

import java.util.List;

public interface MatchService {
    /**
     * Genera automáticamente los partidos de la primera jornada para una competición.
     * <p>
     * Se emparejan los equipos registrados en la competición de manera aleatoria,
     * respetando el número de pistas y el máximo de partidos por pista y día.
     * </p>
     *
     * @param competitionId ID de la competición
     * @return lista de {@link Match} generados
     */
    List<Match> generateFirstRound(Long competitionId);


    /**
     * Devuelve todos los partidos de una competición.
     *
     * @param competitionId ID de la competición
     * @return lista de {@link Match} de la competición, puede estar vacía si no se han generado partidos
     */
    List<Match> getMatchesByCompetition(Long competitionId);

    /**
     * Devuelve los equipos que no han sido asignados a ningún partido en la primera jornada
     * de la competición indicada.
     * <p>
     * Se compara la lista de todos los equipos registrados en la competición con los equipos
     * que ya aparecen en los partidos generados.
     * </p>
     *
     * @param competitionId ID de la competición
     * @return lista de {@link Team} que aún no tienen partido asignado
     */
    List<Team> getUnassignedTeams(Long competitionId);
}
