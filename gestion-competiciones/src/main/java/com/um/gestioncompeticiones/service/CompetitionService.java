package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.dto.CompetitionCreateDTO;
import com.um.gestioncompeticiones.model.Competition;
import com.um.gestioncompeticiones.model.Team;

import java.util.List;

public interface CompetitionService {

    /**
     * Crea una nueva competición a partir del DTO proporcionado.
     *
     * @param competitionCreateDTO el DTO que contiene los datos de la competición
     * @return la entidad {@link Competition} creada
     */
    Competition createCompetition(CompetitionCreateDTO competitionCreateDTO);

    /**
     * Recupera todas las competiciones disponibles en el sistema.
     *
     * @return una lista con todas las entidades {@link Competition}, puede estar vacía si no existen competiciones
     */
    List<Competition> getAllCompetitions();

    /**
     * Recupera una competición por su identificador único.
     *
     * @param id el ID de la competición a recuperar
     * @return la entidad {@link Competition} con el ID especificado
     */
    Competition getCompetitionById(Long id);

    /**
     * Recupera todos los equipos registrados en una competición específica.
     *
     * @param id el ID de la competición
     * @return una lista de entidades {@link Team} que participan en la competición,
     *         puede estar vacía si no hay equipos registrados
     */
    List<Team> getTeamsByCompetition(Long id);

}
