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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Services-Servlet", urlPatterns = {
        Routes.SERVICES,
        Routes.SERVICES_GETSERVICE,
        Routes.SERVICES_ADDSERVICE,
        Routes.SERVICES_UPDATESERVICE,
        Routes.SERVICES_DELETESERVICE,
        Routes.SERVICETYPES,
        Routes.SERVICETYPES_GETSERVICETYPE,
        Routes.SERVICETYPES_ADDSERVICETYPE,
        Routes.SERVICETYPES_UPDATESERVICETYPE,
        Routes.SERVICETYPES_DELETESERVICETYPE
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
        } else if(request.getServletPath().equals(Routes.SERVICETYPES)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            // retrieving all the servicetypes
            ArrayList<ServiceTypeDB> serviceTypes = ServiceManagerDB.getManager().getAllServiceTypes();

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(serviceTypes));
        } else if(request.getServletPath().equals(Routes.SERVICETYPES_GETSERVICETYPE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idServiceType")) {
                int idServiceType = Integer.parseInt(pars.get("idServiceType")[0]);

                if(!ServiceManagerDB.getManager().checkIfServiceTypeExists(idServiceType)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service Type not found.");
                } else {
                    ServiceTypeDB serviceType = ServiceManagerDB.getManager().getServiceType(idServiceType);
                    out.println(gson.toJson(serviceType));
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idServiceType must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals(Routes.SERVICES_ADDSERVICE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the ServiceDB class, so we perform the mapping
            // using the Gson library
            ServiceDB s = gson.fromJson(in, ServiceDB.class);

            // input validation
            if(s == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(s.getName() == null || s.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceNameExists(s.getName(), 0, username)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service name already used.");
            } else if(s.getIdServiceType() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter idServiceType is required.");
            } else if(ServiceManagerDB.getManager().getServiceType(s.getIdServiceType()) == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service Type doesn't exist.");
            } else if(ServiceManagerDB.getManager().addNewService(s, username) > 0) {
                // adding the new service, we need to pass the username because
                // we need to retrieve the user's id

                // retrieving the service inserted to return it to as response
                s = ServiceManagerDB.getManager().getService(s.getIdService());
                if(s != null)
                    out.println(gson.toJson(s));
                else
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error retrieving the service after inserting it");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Routes.SERVICETYPES_ADDSERVICETYPE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the ServiceTypeDB class, so we perform the mapping
            // using the Gson library
            ServiceTypeDB st = gson.fromJson(in, ServiceTypeDB.class);

            // first we check if the user is admin
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(st == null) { // input validation
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(st.getName() == null || st.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceTypeNameExists(st.getName(), 0)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service Type already exists.");
            } else if(ServiceManagerDB.getManager().addNewServiceType(st) > 0) {
                // retrieving the servicetype inserted to return it to as response
                st = ServiceManagerDB.getManager().getServiceType(st.getIdServiceType());
                if(st != null)
                    out.println(gson.toJson(st));
                else
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error retrieving the service type after inserting it.");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals(Routes.SERVICES_UPDATESERVICE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the ServiceDB class, so we perform the mapping
            // using the Gson library
            ServiceDB s = gson.fromJson(in, ServiceDB.class);

            // input validation
            if(s == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(s.getIdService() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter idService is required.");
            } else if(!ServiceManagerDB.getManager().serviceExists(s.getIdService())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found.");
            } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(s.getIdService(), username)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User is not owner of service.");
            } else if(s.getName() == null || s.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceNameExists(s.getName(), s.getIdService(), username)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service name already used.");
            } else if(s.getIdServiceType() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter idServiceType is required.");
            } else if(ServiceManagerDB.getManager().getServiceType(s.getIdServiceType()) == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service Type doesn't exist.");
            } else if(!ServiceManagerDB.getManager().updateService(s)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Routes.SERVICETYPES_UPDATESERVICETYPE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the ServiceTypeDB class, so we perform the mapping
            // using the Gson library
            ServiceTypeDB st = gson.fromJson(in, ServiceTypeDB.class);

            // first we check if the user is admin
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(st == null) { // input validation
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(st.getIdServiceType() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter idServiceType is required.");
            } else if(!ServiceManagerDB.getManager().checkIfServiceTypeExists(st.getIdServiceType())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service Type not found.");
            } else if(st.getName() == null || st.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceTypeNameExists(st.getName(), st.getIdServiceType())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service Type name already used.");
            } else if(!ServiceManagerDB.getManager().updateServiceType(st)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.SERVICES_DELETESERVICE)) {
            response.setContentType("application/json");
            String username = LoginService.getCurrentLogin(request.getSession());

            // retrieving of the parameters coming from the querystring
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idService")) {
                int idService = Integer.parseInt(pars.get("idService")[0]);

                if(!ServiceManagerDB.getManager().serviceExists(idService)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found.");
                } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(idService, username)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User is not owner of service.");
                } else if(!ServiceManagerDB.getManager().deleteService(idService)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idService must be provided.");
            }
        } else if(request.getServletPath().equals(Routes.SERVICETYPES_DELETESERVICETYPE)) {
            response.setContentType("application/json");
            String username = LoginService.getCurrentLogin(request.getSession());

            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idServiceType")) {
                int idServiceType = Integer.parseInt(pars.get("idServiceType")[0]);

                if(!ServiceManagerDB.getManager().checkIfServiceTypeExists(idServiceType)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service Type not found.");
                }  else if(!ServiceManagerDB.getManager().deleteServiceType(idServiceType)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idServiceType must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
