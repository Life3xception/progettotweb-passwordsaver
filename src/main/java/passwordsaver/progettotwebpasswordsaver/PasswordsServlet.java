package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Config;
import passwordsaver.progettotwebpasswordsaver.constants.Apis;
import passwordsaver.progettotwebpasswordsaver.model.*;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Passwords-Servlet", urlPatterns = {
        Apis.PASSWORDS,
        Apis.PASSWORDS_GETPASSWORD,
        Apis.PASSWORDS_GETDECODEDPASSWORD,
        Apis.PASSWORDS_GETDETAILEDPASSWORDS,
        Apis.PASSWORDS_GETDETAILEDPASSWORDSBYSERVICE,
        Apis.PASSWORDS_GETSTARREDPASSWORDS,
        Apis.PASSWORDS_GETDETAILEDSTARREDPASSWORDS,
        Apis.PASSWORDS_ADDPASSWORD,
        Apis.PASSWORDS_UPDATEPASSWORD,
        Apis.PASSWORDS_DELETEPASSWORD
})
public class PasswordsServlet extends HttpServlet {
    private Gson gson;

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Apis.PASSWORDS)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // TODO: potrebbe servire per richiesta password per tipo servizio
            // retrieving the parameters from the querystring as a key-value Map
            // Map<String, String[]> params = request.getParameterMap();

            // retrieving all the valid passwords for the user
            ArrayList<PasswordDB> passwords = PasswordManagerDB.getManager().getAllPasswords(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(passwords));
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETPASSWORD)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            boolean isAdmin = loggedUser.getIdUserType() == Config.adminIdUserType;
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idPassword")) {
                int idPwd = Integer.parseInt(pars.get("idPassword")[0]);

                if(!PasswordManagerDB.getManager().passwordExists(idPwd)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Password not found.");
                } else {
                    PasswordDB pwd = PasswordManagerDB.getManager().getPassword(idPwd);

                    if(pwd.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Could not get data of password of another user.");
                    }

                    out.println(gson.toJson(pwd));
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idPassword must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDECODEDPASSWORD)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idPassword")) {
                int idPwd = Integer.parseInt(pars.get("idPassword")[0]);

                if(!PasswordManagerDB.getManager().passwordExists(idPwd)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Password not found.");
                } else {
                    PasswordDB pwd = PasswordManagerDB.getManager().getDecodedPassword(idPwd);

                    if(pwd.getIdUser() != loggedUser.getIdUser()) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Could not get data of password of another user.");
                    }

                    out.println(gson.toJson(pwd));
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idPassword must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDETAILEDPASSWORDS)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // TODO: potrebbe servire per richiesta password per tipo servizio
            // retrieving the parameters from the querystring as a key-value Map
            // Map<String, String[]> params = request.getParameterMap();

            // retrieving all the valid passwords for the user
            ArrayList<DetailedPasswordDB> passwords = PasswordManagerDB.getManager().getAllDetailedPasswords(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(passwords));
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDETAILEDPASSWORDSBYSERVICE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // retrieving the parameters from the querystring as a key-value Map
            Map<String, String[]> params = request.getParameterMap();

            if(params.containsKey("idService")) {
                int idService = Integer.parseInt(params.get("idService")[0]);

                if(!ServiceManagerDB.getManager().serviceExists(idService)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found.");
                } else {
                    // retrieving all the valid passwords by service for the user
                    ArrayList<DetailedPasswordDB> passwords = PasswordManagerDB.getManager().getAllDetailedPasswordsByService(username, idService);

                    // returning the arraylist as an array of JsonObject using the Gson library
                    out.println(gson.toJson(passwords));
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idService must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETSTARREDPASSWORDS)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // retrieving of the parameters coming from the querystring
            Map<String, String[]> pars = request.getParameterMap();
            int limit = 0;

            if(pars.containsKey("limit")) {
                limit = Integer.parseInt(pars.get("limit")[0]);
            }

            // retrieving all (or limited) valid starred passwords for the user
            ArrayList<PasswordDB> passwords = PasswordManagerDB.getManager().getAllStarredPasswords(username, limit);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(passwords));
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDETAILEDSTARREDPASSWORDS)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // retrieving of the parameters coming from the querystring
            Map<String, String[]> pars = request.getParameterMap();
            int limit = 0;

            if(pars.containsKey("limit")) {
                limit = Integer.parseInt(pars.get("limit")[0]);
            }

            // retrieving all (or limited) valid starred passwords for the user
            ArrayList<DetailedPasswordDB> passwords = PasswordManagerDB.getManager().getAllDetailedStarredPasswords(username, limit);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(passwords));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Apis.PASSWORDS_ADDPASSWORD)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request);

            // in the body of the request we expect to have the data
            // corresponding to the PasswordDB class, so we perform the mapping
            // using the Gson library
            PasswordDB p = gson.fromJson(in, PasswordDB.class);

            // input validation
            if(p == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(p.getName() == null || p.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter name is required.");
            } else if(p.getPassword() == null || p.getPassword().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter password is required.");
            } else if(!p.getEmail().isEmpty() && !p.getEmail().matches(Config.EMAIL_PATTERN)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email address.");
            } else if(p.getIdService() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter idService is required.");
            } else if(ServiceManagerDB.getManager().getService(p.getIdService()) == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service doesn't exist.");
            } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(p.getIdService(), username)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User is not owner of service.");
            } else if(PasswordManagerDB.getManager().addNewPassword(p, username) > 0) {
                // adding the new password, we need to pass the username because
                // we need to retrieve the user's id

                // retrieving the password inserted to return it to as response
                p = PasswordManagerDB.getManager().getPassword(p.getIdPassword());
                if(p != null)
                    out.println(gson.toJson(p));
                else
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error retrieving the password after inserting it");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Apis.PASSWORDS_UPDATEPASSWORD)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            String username = LoginService.getCurrentLogin(request);

            // in the body of the request we expect to have the data
            // corresponding to the PasswordDB class, so we perform the mapping
            // using the Gson library
            PasswordDB p = gson.fromJson(in, PasswordDB.class);

            // input validation
            if(p == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(p.getIdPassword() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter idPassword is required.");
            } else if(!PasswordManagerDB.getManager().passwordExists(p.getIdPassword())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Password not found.");
            } else if(!PasswordManagerDB.getManager().userIsOwnerOfPassword(p.getIdPassword(), username)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User is not owner of password.");
            } else if(p.getName() == null || p.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter name is required.");
            } else if(p.getPassword() == null || p.getPassword().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter password is required.");
            } else if(!p.getEmail().isEmpty() && !p.getEmail().matches(Config.EMAIL_PATTERN)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email address.");
            } else if(p.getIdService() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter idService is required.");
            } else if(ServiceManagerDB.getManager().getService(p.getIdService()) == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Service doesn't exist.");
            } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(p.getIdService(), username)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User is not owner of service.");
            } else if(!PasswordManagerDB.getManager().updatePassword(p, username)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Apis.PASSWORDS_DELETEPASSWORD)) {
            response.setContentType("application/json");
            String username = LoginService.getCurrentLogin(request);

            // retrieving of the parameters coming from the querystring
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idPassword")) {
                int idPwd = Integer.parseInt(pars.get("idPassword")[0]);

                if(!PasswordManagerDB.getManager().passwordExists(idPwd)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Password not found.");
                } else if(!PasswordManagerDB.getManager().userIsOwnerOfPassword(idPwd, username)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User is not owner of password.");
                } else if(!PasswordManagerDB.getManager().deletePassword(idPwd)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idPassword must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
