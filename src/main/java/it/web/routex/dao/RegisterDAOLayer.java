package it.web.routex.dao;


import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.domain.CredentialsDTO;

public abstract class RegisterDAOLayer {
    public abstract CredentialsDTO save(Object cred) throws CredentialsExceptionRemoli;
}