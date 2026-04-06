package it.web.routex.controller.applicativo;

import it.web.routex.bean.AdminUserBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import it.web.routex.utility.singleton.Credentials;

import java.util.ArrayList;
import java.util.List;

public class ManageAdminsControllerApplicativo {

    public List<AdminUserBean> listAdmins() throws DAOExceptionRemoli {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        return toBeans(layer.listAdmins());
    }

    public List<AdminUserBean> listTravelers() throws DAOExceptionRemoli {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        return toBeans(layer.listTravelers());
    }

    public void createAdmin(String nome,
                            String cognome,
                            String email,
                            String password,
                            String codiceFiscale) throws DAOExceptionRemoli {
        String normalizedNome = normalizeRequired(nome, "Name");
        String normalizedCognome = normalizeRequired(cognome, "Surname");
        String normalizedEmail = normalizeRequired(email, "Email").toLowerCase();
        String normalizedPassword = normalizeRequired(password, "Password");
        String normalizedCf = normalizeRequired(codiceFiscale, "Tax code").toUpperCase();

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        for (Credentials existingAdmin : layer.listAdmins()) {
            if (normalizedEmail.equalsIgnoreCase(existingAdmin.getEmail())) {
                throw new DAOExceptionRemoli("An admin with the same email already exists.");
            }
            if (normalizedCf.equalsIgnoreCase(existingAdmin.getCodiceFiscale())) {
                throw new DAOExceptionRemoli("An admin with the same tax code already exists.");
            }
        }

        layer.createAdmin(normalizedNome, normalizedCognome, normalizedEmail, normalizedPassword, normalizedCf);
    }

    public int deleteAdmins(List<String> codiciFiscali, String currentAdminCf) throws DAOExceptionRemoli {
        if (codiciFiscali == null || codiciFiscali.isEmpty()) {
            throw new DAOExceptionRemoli("Select at least one admin to delete.");
        }

        List<String> normalized = new ArrayList<>();
        for (String cf : codiciFiscali) {
            if (cf != null && !cf.trim().isEmpty()) {
                normalized.add(cf.trim().toUpperCase());
            }
        }

        if (normalized.isEmpty()) {
            throw new DAOExceptionRemoli("Select at least one admin to delete.");
        }

        if (currentAdminCf != null && normalized.contains(currentAdminCf.trim().toUpperCase())) {
            throw new DAOExceptionRemoli("You cannot delete the admin account currently in use.");
        }

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        return layer.deleteAdmins(normalized);
    }

    public int deleteTravelers(List<String> codiciFiscali) throws DAOExceptionRemoli {
        List<String> normalized = normalizeCodes(codiciFiscali);
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        return layer.deleteTravelers(normalized);
    }

    private String normalizeRequired(String value, String fieldName) throws DAOExceptionRemoli {
        if (value == null || value.trim().isEmpty()) {
            throw new DAOExceptionRemoli(fieldName + " is required.");
        }
        return value.trim();
    }

    private List<String> normalizeCodes(List<String> values) throws DAOExceptionRemoli {
        if (values == null || values.isEmpty()) {
            throw new DAOExceptionRemoli("Select at least one account.");
        }

        List<String> normalized = new ArrayList<>();
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                normalized.add(value.trim().toUpperCase());
            }
        }

        if (normalized.isEmpty()) {
            throw new DAOExceptionRemoli("Select at least one account.");
        }
        return normalized;
    }

    private List<AdminUserBean> toBeans(List<Credentials> credentials) {
        List<AdminUserBean> beans = new ArrayList<>();
        for (Credentials credential : credentials) {
            AdminUserBean bean = new AdminUserBean();
            bean.setNome(credential.getNome());
            bean.setCognome(credential.getCognome());
            bean.setEmail(credential.getEmail());
            bean.setCodiceFiscale(credential.getCodiceFiscale());
            beans.add(bean);
        }
        return beans;
    }
}
