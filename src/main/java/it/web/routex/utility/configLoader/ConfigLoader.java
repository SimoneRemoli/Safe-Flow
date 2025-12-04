package it.web.routex.utility.configLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private static final Properties props = new Properties();

    private ConfigLoader()
    {
        throw new AssertionError("Classe che legge il path.properties - new proibito");
    }

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream("path.properties")) {

            if (is == null) {
                logger.error("File path.properties non trovato nel classpath!");
                throw new RuntimeException("Impossibile trovare path.properties nel classpath");
            }

            props.load(is);
            logger.info("File path.properties caricato correttamente.");

        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento path.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
