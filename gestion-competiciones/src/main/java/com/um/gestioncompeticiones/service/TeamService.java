package com.um.gestioncompeticiones.service;

import com.um.gestioncompeticiones.dto.TeamCreateDTO;
import com.um.gestioncompeticiones.model.Team;

import java.util.List;

public interface TeamService {

    /**
     * Crea un nuevo equipo a partir del DTO proporcionado.
     *
     * @param teamCreateDTO el DTO que contiene los datos del equipo a crear
     * @return la entidad {@link Team} creada
     */
    Team createTeam(TeamCreateDTO teamCreateDTO);

    /**
     * Registra un equipo en una competición específica.
     *
     * @param teamId        el ID del equipo a registrar
     * @param competitionId el ID de la competición donde se registrará el equipo
     * @return el equipo {@link Team} con la relación actualizada
     */
    Team registerTeamToCompetition(Long teamId, Long competitionId);


    /**
     * Recupera todos los equipos existentes en el sistema.
     *
     * @return una lista de entidades {@link Team}, puede estar vacía si no hay equipos
     */
    List<Team> getAllTeams();

    /**
     * Recupera un equipo por su identificador único.
     *
     * @param id el ID del equipo a recuperar
     * @return la entidad {@link Team} correspondiente al ID
     */
    Team getTeamById(Long id);
}
