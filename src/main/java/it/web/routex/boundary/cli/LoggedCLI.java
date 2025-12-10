package it.web.routex.boundary.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggedCLI {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
