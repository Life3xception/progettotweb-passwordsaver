package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Config;
import passwordsaver.progettotwebpasswordsaver.constants.Routes;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;
import passwordsaver.progettotwebpasswordsaver.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Services-Servlet", urlPatterns = {
        Routes.SERVICES,
        Routes.SERVICES_GETSERVICE,
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
        } else if(request.getServletPath().equals(Routes.SERVICES_GETSERVICE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            boolean isAdmin = loggedUser.getIdUserType() == Config.adminIdUserType;
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idService")) {
                int idService = Integer.parseInt(pars.get("idService")[0]);

                if(!ServiceManagerDB.getManager().serviceExists(idService)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found.");
                } else {
                    ServiceDB service = ServiceManagerDB.getManager().getService(idService);

                    if(service.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Could not get data of service of another user.");
                    }

                    out.println(gson.toJson(service));
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idService must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
