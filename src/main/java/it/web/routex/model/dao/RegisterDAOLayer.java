package it.web.routex.model.dao;


import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.model.domain.CredentialsDTO;

public abstract class RegisterDAOLayer {
    public abstract CredentialsDTO save(Object cred) throws CredentialsExceptionRemoli;
}