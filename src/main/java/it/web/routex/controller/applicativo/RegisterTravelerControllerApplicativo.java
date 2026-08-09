package it.web.routex.controller.applicativo;

import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RegisterTravelerControllerApplicativo {

    private static final String EMAIL_REGEX = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";

    public void register(String firstName,
                         String lastName,
                         String taxCode,
                         String email,
                         String password,
                         String birthDate,
                         boolean disabled) throws BrondiException, DAOExceptionRemoli {

        String normalizedFirstName = require(firstName, "First name");
        String normalizedLastName = require(lastName, "Last name");
        String normalizedTaxCode = require(taxCode, "Tax code").toUpperCase();
        String normalizedEmail = require(email, "Email").toLowerCase();
        String normalizedPassword = require(password, "Password");
        LocalDate normalizedBirthDate = parseBirthDate(birthDate);

        if (normalizedTaxCode.length() != 16) {
            throw validationError("Tax code must contain 16 characters.");
        }

        if (!normalizedEmail.matches(EMAIL_REGEX)) {
            throw validationError("Insert a valid email address.");
        }

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        layer.registerTraveler(
                normalizedFirstName,
                normalizedLastName,
                normalizedTaxCode,
                normalizedEmail,
                normalizedPassword,
                Date.valueOf(normalizedBirthDate),
                disabled
        );
    }

    private String require(String value, String fieldName) throws BrondiException {
        if (value == null || value.trim().isEmpty()) {
            throw validationError(fieldName + " is required.");
        }
        return value.trim();
    }

    private LocalDate parseBirthDate(String birthDate) throws BrondiException {
        String normalizedBirthDate = require(birthDate, "Birth date");

        try {
            LocalDate parsed = LocalDate.parse(normalizedBirthDate);
            if (parsed.isAfter(LocalDate.now())) {
                throw validationError("Birth date cannot be in the future.");
            }
            return parsed;
        } catch (DateTimeParseException e) {
            throw validationError("Insert a valid birth date.");
        }
    }

    private BrondiException validationError(String message) {
        return new BrondiException(message, "REGISTRATION_VALIDATION", message);
    }
}
