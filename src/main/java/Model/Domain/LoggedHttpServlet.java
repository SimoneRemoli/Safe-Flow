package Model.Domain;
import javax.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggedHttpServlet extends HttpServlet {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
