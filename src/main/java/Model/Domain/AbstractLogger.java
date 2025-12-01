package Model.Domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLogger {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
}

/*
getClass() NON è la classe astratta.
È la classe reale che sta estendendo questo logger. @SimoneRemoli
 */