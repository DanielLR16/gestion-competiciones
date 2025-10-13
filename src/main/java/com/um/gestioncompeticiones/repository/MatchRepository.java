package com.um.gestioncompeticiones.repository;

import com.um.gestioncompeticiones.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository  extends JpaRepository<Match, Long> {

}
