package com.um.gestioncompeticiones.repository;

import com.um.gestioncompeticiones.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    // Comprobar si ya existe una competición con un nombre concreto
    boolean existsByName(String name);
}
