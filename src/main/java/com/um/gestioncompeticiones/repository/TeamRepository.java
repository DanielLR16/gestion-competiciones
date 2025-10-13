package com.um.gestioncompeticiones.repository;

import com.um.gestioncompeticiones.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    // Buscar un equipo por nombre
    Optional<Team> findByName(String name);

    // Verificar si un equipo ya está registrado en una competición concreta
    @Query("""
        SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
        FROM Team t
        JOIN t.competitions c
        WHERE t.id = :teamId AND c.id = :competitionId
    """)
    boolean existsByCompetition(Long teamId, Long competitionId);
}
