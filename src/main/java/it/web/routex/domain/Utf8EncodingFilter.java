package it.web.routex.domain;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Utf8EncodingFilter implements Filter {

    private static final String UTF_8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
        // No configuration required.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(UTF_8);
        }
        response.setCharacterEncoding(UTF_8);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No resources to release.
    }
}
