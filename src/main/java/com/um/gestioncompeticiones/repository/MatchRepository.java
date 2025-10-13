package com.um.gestioncompeticiones.repository;

import com.um.gestioncompeticiones.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository  extends JpaRepository<Match, Long> {

    // Obtener todos los partidos de una competición
    List<Match> findByCompetitionId(Long competitionId);

    // Obtener los partidos de una competición en una fecha concreta (útil para primera jornada)
    List<Match> findByCompetitionIdAndMatchDate(Long competitionId, LocalDate matchDate);
}
