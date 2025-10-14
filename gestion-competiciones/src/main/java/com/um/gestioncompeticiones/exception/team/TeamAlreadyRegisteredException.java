package com.um.gestioncompeticiones.exception.team;

public class TeamAlreadyRegisteredException extends RuntimeException {
    public TeamAlreadyRegisteredException(String message) {
        super(message);
    }
}
