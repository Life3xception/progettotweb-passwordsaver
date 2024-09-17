package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Config;
import passwordsaver.progettotwebpasswordsaver.constants.Apis;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;
import passwordsaver.progettotwebpasswordsaver.model.*;
import passwordsaver.progettotwebpasswordsaver.utils.JsonErrorResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Services-Servlet", urlPatterns = {
        Apis.SERVICES,
        Apis.SERVICES_GETSERVICE,
        Apis.SERVICES_GETMOSTUSEDSERVICESBYUSER,
        Apis.SERVICES_GETDETAILEDSERVICES,
        Apis.SERVICES_GETDETAILEDSERVICESBYSERVICETYPE,
        Apis.SERVICES_GETDETAILEDSERVICE,
        Apis.SERVICES_ADDSERVICE,
        Apis.SERVICES_UPDATESERVICE,
        Apis.SERVICES_DELETESERVICE,
        Apis.SERVICETYPES,
        Apis.SERVICETYPES_GETSERVICETYPE,
        Apis.SERVICETYPES_ADDSERVICETYPE,
        Apis.SERVICETYPES_UPDATESERVICETYPE,
        Apis.SERVICETYPES_DELETESERVICETYPE
})
public class ServicesServlet extends HttpServlet {
    private Gson gson;

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals(Apis.SERVICES)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // TODO: potrebbe servire per richiesta servizi per tipo servizio
            // retrieving the parameters from the querystring as a key-value Map
            // Map<String, String[]> params = request.getParameterMap();

            // retrieving all the valid services for the user
            ArrayList<ServiceDB> services = ServiceManagerDB.getManager().getAllServices(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(services));
        } else if(request.getServletPath().equals(Apis.SERVICES_GETDETAILEDSERVICES)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // TODO: potrebbe servire per richiesta servizi per tipo servizio
            // retrieving the parameters from the querystring as a key-value Map
            // Map<String, String[]> params = request.getParameterMap();

            // retrieving all the valid services for the user
            ArrayList<DetailedServiceDB> services = ServiceManagerDB.getManager().getAllDetailedServices(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(services));
        } else if(request.getServletPath().equals(Apis.SERVICES_GETDETAILEDSERVICESBYSERVICETYPE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            Map<String, String[]> pars = request.getParameterMap();
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            if(pars.containsKey("idServiceType")) {
                int idServiceType = Integer.parseInt(pars.get("idServiceType")[0]);

                if(!ServiceManagerDB.getManager().checkIfServiceTypeExists(idServiceType, isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting services by service type", "Service Type not found.");
                } else {
                    // retrieving all the valid detailed services for the user by service type
                    ArrayList<DetailedServiceDB> services = ServiceManagerDB.getManager().getAllDetailedServicesByServiceType(username, idServiceType);

                    // returning the arraylist as an array of JsonObject using the Gson library
                    out.println(gson.toJson(services));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting services by service type", "idServiceType must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.SERVICES_GETSERVICE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            boolean isAdmin = loggedUser.getIdUserType() == Config.adminIdUserType;
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idService")) {
                int idService = Integer.parseInt(pars.get("idService")[0]);

                if(!ServiceManagerDB.getManager().serviceExists(idService)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting service", "Service not found.");
                } else {
                    ServiceDB service = ServiceManagerDB.getManager().getService(idService);

                    if(service.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error getting service", "Could not get data of service of another user.");
                    }

                    out.println(gson.toJson(service));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting service", "idService must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.SERVICES_GETDETAILEDSERVICE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            boolean isAdmin = loggedUser.getIdUserType() == Config.adminIdUserType;
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idService")) {
                int idService = Integer.parseInt(pars.get("idService")[0]);

                if(!ServiceManagerDB.getManager().serviceExists(idService)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting service", "Service not found.");
                } else {
                    DetailedServiceDB service = ServiceManagerDB.getManager().getDetailedService(idService);

                    if(service.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error getting service", "Could not get data of service of another user.");
                    }

                    out.println(gson.toJson(service));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting service", "idService must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.SERVICES_GETMOSTUSEDSERVICESBYUSER)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // retrieving of the parameters coming from the querystring
            Map<String, String[]> pars = request.getParameterMap();
            int limit = 0;

            if(pars.containsKey("limit")) {
                limit = Integer.parseInt(pars.get("limit")[0]);
            }

            // retrieving most used services for the user
            ArrayList<ServiceDB> services = ServiceManagerDB.getManager().getMostUsedServicesByUser(username, limit);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(services));
        } else if(request.getServletPath().equals(Apis.SERVICETYPES)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            // retrieving all the servicetypes
            ArrayList<ServiceTypeDB> serviceTypes = ServiceManagerDB.getManager().getAllServiceTypes(isAdmin);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(serviceTypes));
        } else if(request.getServletPath().equals(Apis.SERVICETYPES_GETSERVICETYPE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            // only admin users can access this API
            if(!isAdmin)
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idServiceType")) {
                int idServiceType = Integer.parseInt(pars.get("idServiceType")[0]);

                if(!ServiceManagerDB.getManager().checkIfServiceTypeExists(idServiceType, isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting service type", "Service Type not found.");
                } else {
                    // mettiamo isAdmin=true perché sappiamo che a questa API possono accedere solo gli admin
                    ServiceTypeDB serviceType = ServiceManagerDB.getManager().getServiceType(idServiceType, isAdmin);
                    out.println(gson.toJson(serviceType));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting service type", "idServiceType must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals(Apis.SERVICES_ADDSERVICE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            // in the body of the request we expect to have the data
            // corresponding to the ServiceDB class, so we perform the mapping
            // using the Gson library
            ServiceDB s = gson.fromJson(in, ServiceDB.class);

            // input validation
            if(s == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service", "Empty request body.");
            } else if(s.getName() == null || s.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service", "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceNameExists(s.getName(), 0, username)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service", "Service name already used.");
            } else if(s.getIdServiceType() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service", "Parameter idServiceType is required.");
            } else if(ServiceManagerDB.getManager().getServiceType(s.getIdServiceType(), isAdmin) == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service", "Service Type doesn't exist.");
            } else if(ServiceManagerDB.getManager().addNewService(s, username) > 0) {
                // adding the new service, we need to pass the username because
                // we need to retrieve the user's id

                // retrieving the service inserted to return it to as response
                s = ServiceManagerDB.getManager().getService(s.getIdService());
                if(s != null)
                    out.println(gson.toJson(s));
                else
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error adding service", "Error retrieving the service after inserting it");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Apis.SERVICETYPES_ADDSERVICETYPE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            // in the body of the request we expect to have the data
            // corresponding to the ServiceTypeDB class, so we perform the mapping
            // using the Gson library
            ServiceTypeDB st = gson.fromJson(in, ServiceTypeDB.class);

            // first we check if the user is admin
            if(!isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(st == null) { // input validation
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service type", "Empty request body.");
            } else if(st.getName() == null || st.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service type", "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceTypeNameExists(st.getName(), 0)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding service type", "Service Type already exists.");
            } else if(ServiceManagerDB.getManager().addNewServiceType(st) > 0) {
                // retrieving the servicetype inserted to return it to as response
                st = ServiceManagerDB.getManager().getServiceType(st.getIdServiceType(), isAdmin);
                if(st != null)
                    out.println(gson.toJson(st));
                else
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error adding service type", "Error retrieving the service type after inserting it.");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals(Apis.SERVICES_UPDATESERVICE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            // in the body of the request we expect to have the data
            // corresponding to the ServiceDB class, so we perform the mapping
            // using the Gson library
            ServiceDB s = gson.fromJson(in, ServiceDB.class);

            // input validation
            if(s == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service", "Empty request body.");
            } else if(s.getIdService() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service", "Parameter idService is required.");
            } else if(!ServiceManagerDB.getManager().serviceExists(s.getIdService())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error updating service", "Service not found.");
            } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(s.getIdService(), username)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service", "User is not owner of service.");
            } else if(s.getName() == null || s.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service", "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceNameExists(s.getName(), s.getIdService(), username)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service", "Service name already used.");
            } else if(s.getIdServiceType() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service", "Parameter idServiceType is required.");
            } else if(ServiceManagerDB.getManager().getServiceType(s.getIdServiceType(), isAdmin) == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service", "Service Type doesn't exist.");
            } else if(!ServiceManagerDB.getManager().updateService(s)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Apis.SERVICETYPES_UPDATESERVICETYPE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            String username = LoginService.getCurrentLogin(request);
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            // in the body of the request we expect to have the data
            // corresponding to the ServiceTypeDB class, so we perform the mapping
            // using the Gson library
            ServiceTypeDB st = gson.fromJson(in, ServiceTypeDB.class);

            // first we check if the user is admin
            if(!isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(st == null) { // input validation
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service type", "Empty request body.");
            } else if(st.getIdServiceType() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service type", "Parameter idServiceType is required.");
            } else if(!ServiceManagerDB.getManager().checkIfServiceTypeExists(st.getIdServiceType(), isAdmin)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error updating service type", "Service Type not found.");
            } else if(st.getName() == null || st.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service type", "Parameter name is required.");
            } else if(ServiceManagerDB.getManager().checkIfServiceTypeNameExists(st.getName(), st.getIdServiceType())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating service type", "Service Type name already used.");
            } else if(!ServiceManagerDB.getManager().updateServiceType(st, isAdmin)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Apis.SERVICES_DELETESERVICE)) {
            response.setContentType("application/json");
            String username = LoginService.getCurrentLogin(request);

            // retrieving of the parameters coming from the querystring
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idService")) {
                int idService = Integer.parseInt(pars.get("idService")[0]);

                if(!ServiceManagerDB.getManager().serviceExists(idService)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error deleting service", "Service not found.");
                } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(idService, username)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error deleting service", "User is not owner of service.");
                } else if(!ServiceManagerDB.getManager().deleteService(idService)) { // TODO: devo fare anche annullamento di tutte le password!
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error deleting service", "idService must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.SERVICETYPES_DELETESERVICETYPE)) {
            response.setContentType("application/json");
            String username = LoginService.getCurrentLogin(request);
            boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

            // only admin users can access this API
            if(!isAdmin)
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idServiceType")) {
                int idServiceType = Integer.parseInt(pars.get("idServiceType")[0]);

                if(!ServiceManagerDB.getManager().checkIfServiceTypeExists(idServiceType, isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error deleting service type", "Service Type not found.");
                }  else if(!ServiceManagerDB.getManager().deleteServiceType(idServiceType)) { // TODO: devo fare anche annullamento di tutti i service
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error deleting service type", "idServiceType must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
