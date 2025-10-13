package com.um.gestioncompeticiones.exception.competition;

public class CompetitionNotFoundException extends RuntimeException {

    public CompetitionNotFoundException(String message) {
        super(message);
    }
}