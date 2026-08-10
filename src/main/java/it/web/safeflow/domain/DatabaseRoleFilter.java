package it.web.safeflow.domain;

import it.web.safeflow.enumerator.Ruolo;
import it.web.safeflow.utility.factory.ConnectionFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class DatabaseRoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            ConnectionFactory.setRequestRole(resolveRole(request));
            chain.doFilter(request, response);
        } finally {
            ConnectionFactory.clearRequestRole();
        }
    }

    private Ruolo resolveRole(ServletRequest request) {
        if (!(request instanceof HttpServletRequest httpRequest)) {
            return Ruolo.LOGIN;
        }

        return SessionAuthUtil.ruolo(httpRequest.getSession(false))
                .flatMap(this::toRole)
                .orElse(Ruolo.LOGIN);
    }

    private java.util.Optional<Ruolo> toRole(String role) {
        try {
            return java.util.Optional.of(Ruolo.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return java.util.Optional.empty();
        }
    }
}
