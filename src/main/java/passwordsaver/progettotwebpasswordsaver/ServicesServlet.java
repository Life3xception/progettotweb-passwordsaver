package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Routes;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;
import passwordsaver.progettotwebpasswordsaver.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "Services-Servlet", urlPatterns = {
        Routes.SERVICES
})
public class ServicesServlet extends HttpServlet {
    private Gson gson;

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals(Routes.SERVICES)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // TODO: potrebbe servire per richiesta servizi per tipo servizio
            // retrieving the parameters from the querystring as a key-value Map
            // Map<String, String[]> params = request.getParameterMap();

            // retrieving all the valid services for the user
            ArrayList<ServiceDB> services = ServiceManagerDB.getManager().getAllServices(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(services));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
