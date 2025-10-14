package com.um.gestioncompeticiones.exception.competition;

public class CompetitionAlreadyExistsException extends RuntimeException {

    public CompetitionAlreadyExistsException(String message) {
        super(message);
    }
}
