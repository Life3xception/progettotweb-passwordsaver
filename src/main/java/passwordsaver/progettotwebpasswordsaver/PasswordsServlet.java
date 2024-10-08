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
import passwordsaver.progettotwebpasswordsaver.utils.JsonErrorResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Passwords-Servlet", urlPatterns = {
        Apis.PASSWORDS,
        Apis.PASSWORDS_GETPASSWORD,
        Apis.PASSWORDS_GETDETAILEDPASSWORD,
        Apis.PASSWORDS_GETDETAILEDPASSWORDS,
        Apis.PASSWORDS_GETDECODEDPASSWORD,
        Apis.PASSWORDS_GETDETAILEDPASSWORDSBYSERVICE,
        Apis.PASSWORDS_GETDETAILEDDELETEDPASSWORDSBYUSER,
        Apis.PASSWORDS_GETSTARREDPASSWORDS,
        Apis.PASSWORDS_GETDETAILEDSTARREDPASSWORDS,
        Apis.PASSWORDS_ADDPASSWORD,
        Apis.PASSWORDS_UPDATEPASSWORD,
        Apis.PASSWORDS_RECOVERPASSWORD,
        Apis.PASSWORDS_DELETEPASSWORD
})
public class PasswordsServlet extends HttpServlet {
    private Gson gson;

    /*
     *  TODO:
     *   - [IMP] aggiungere eye nei campi password per vederla nasconderla (login, signup, changepwd, aggiunta utente)
     *   - [IMP] lato admin, tutte le operazioni di ripristino (password cancellate, altro?) (FE)
     *   - [IMP] campi password devono avere tutti tipo password, non text
     *   - [IMP] aggiungere funzione di annullamento ricorsivo su cancellazione tiposerv/tipoutente/serv/utente
     *   - [quasi IMP] attivare https!!
     *   - capire se in signup/adduser/updateuser le pwd devono arrivare dal fe criptate o no
     *   - api di reset di IV e key dell'utente (?)
     *   - vedere se aggiungere sezione logs per admin su FE
     * */

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String username = LoginService.getCurrentLogin(request);
        boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

        if(request.getServletPath().equals(Apis.PASSWORDS)) {
            // retrieving all the valid passwords for the user
            ArrayList<PasswordDB> passwords = PasswordManagerDB.getManager().getAllPasswords(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(passwords));
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETPASSWORD)) {
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idPassword")) {
                int idPwd = Integer.parseInt(pars.get("idPassword")[0]);

                if(!PasswordManagerDB.getManager().passwordExists(username, idPwd, false)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting password", "Password not found");
                } else {
                    PasswordDB pwd = PasswordManagerDB.getManager().getPassword(username, idPwd);

                    if(pwd.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error getting password", "Could not get data of password of another user.");
                    }

                    out.println(gson.toJson(pwd));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting password", "idPassword must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDETAILEDPASSWORD)) {
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idPassword")) {
                int idPwd = Integer.parseInt(pars.get("idPassword")[0]);

                if(!PasswordManagerDB.getManager().passwordExists(username, idPwd, false)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting password", "Password not found");
                } else {
                    DetailedPasswordDB pwd = PasswordManagerDB.getManager().getDetailedPassword(username, idPwd);

                    if(pwd.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error getting password", "Could not get data of password of another user.");
                    }

                    out.println(gson.toJson(pwd));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting password", "idPassword must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDECODEDPASSWORD)) {
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idPassword")) {
                int idPwd = Integer.parseInt(pars.get("idPassword")[0]);

                if(!PasswordManagerDB.getManager().passwordExists(username, idPwd, false)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting decoded password", "Password not found.");
                } else {
                    PasswordDB pwd = PasswordManagerDB.getManager().getDecodedPassword(username, idPwd);

                    if(pwd.getIdUser() != loggedUser.getIdUser()) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error getting password", "Could not get data of password of another user.");
                    }

                    out.println(gson.toJson(pwd));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting decoded password", "idPassword must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDETAILEDPASSWORDS)) {
            // retrieving all the valid passwords for the user
            ArrayList<DetailedPasswordDB> passwords = PasswordManagerDB.getManager().getAllDetailedPasswords(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(passwords));
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDETAILEDPASSWORDSBYSERVICE)) {
            // retrieving the parameters from the querystring as a key-value Map
            Map<String, String[]> params = request.getParameterMap();

            if(params.containsKey("idService")) {
                int idService = Integer.parseInt(params.get("idService")[0]);

                if(!ServiceManagerDB.getManager().serviceExists(username, idService, false)) { // non vogliamo che il fatto che un admin possa aver inserito service non validi intacchi le password
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting passwords by service", "Service not found.");
                } else {
                    // retrieving all the valid passwords by service for the user
                    ArrayList<DetailedPasswordDB> passwords = PasswordManagerDB.getManager().getAllDetailedPasswordsByService(username, idService);

                    // returning the arraylist as an array of JsonObject using the Gson library
                    out.println(gson.toJson(passwords));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting passwords by service", "idService must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETDETAILEDDELETEDPASSWORDSBYUSER)) {
            // retrieving the parameters from the querystring as a key-value Map
            Map<String, String[]> params = request.getParameterMap();

            if(isAdmin) {
                if(params.containsKey("idUser")) {
                    int idUser = Integer.parseInt(params.get("idUser")[0]);

                    if(!UserManagerDB.getManager().userExists(username, idUser, isAdmin)) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting deleted passwords by user", "User not found.");
                    } else {
                        // retrieving all the deleted passwords by service for the user
                        ArrayList<DetailedPasswordDB> passwords = PasswordManagerDB.getManager().getAllDetailedDeletedPasswordsByUser(username, idUser);

                        // returning the arraylist as an array of JsonObject using the Gson library
                        out.println(gson.toJson(passwords));
                    }
                } else {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting deleted passwords by user", "idUser must be provided.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_GETSTARREDPASSWORDS)) {
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
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding password", "Empty request body.");
            } else if(p.getName() == null || p.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding password", "Parameter name is required.");
            } else if(p.getPassword() == null || p.getPassword().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding password", "Parameter password is required.");
            } else if(!p.getEmail().isEmpty() && !p.getEmail().matches(Config.EMAIL_PATTERN)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding password", "Invalid email address.");
            } else if(p.getIdService() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding password", "Parameter idService is required.");
            } else if(ServiceManagerDB.getManager().getService(username, p.getIdService(), false) == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding password", "Service doesn't exist.");
            } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(p.getIdService(), username, false)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding password", "User is not owner of service.");
            } else if(PasswordManagerDB.getManager().addNewPassword(p, username) > 0) {
                // adding the new password, we need to pass the username because
                // we need to retrieve the user's id

                // retrieving the password inserted to return it to as response
                p = PasswordManagerDB.getManager().getPassword(username, p.getIdPassword());
                if(p != null)
                    out.println(gson.toJson(p));
                else
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error adding password", "Error retrieving the password after inserting it");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BufferedReader in = request.getReader();
        String username = LoginService.getCurrentLogin(request);
        boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

        if(request.getServletPath().equals(Apis.PASSWORDS_UPDATEPASSWORD)) {
            // in the body of the request we expect to have the data
            // corresponding to the PasswordDB class, so we perform the mapping
            // using the Gson library
            PasswordDB p = gson.fromJson(in, PasswordDB.class);

            // input validation
            if(p == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "Empty request body.");
            } else if(p.getIdPassword() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "Parameter idPassword is required.");
            } else if(!PasswordManagerDB.getManager().passwordExists(username, p.getIdPassword(), false)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error updating password", "Password not found.");
            } else if(!PasswordManagerDB.getManager().userIsOwnerOfPassword(p.getIdPassword(), username)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "User is not owner of password.");
            } else if(p.getName() == null || p.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "Parameter name is required.");
            } else if(p.getPassword() == null || p.getPassword().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "Parameter password is required.");
            } else if(!p.getEmail().isEmpty() && !p.getEmail().matches(Config.EMAIL_PATTERN)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "Invalid email address.");
            } else if(p.getIdService() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "Parameter idService is required.");
            } else if(ServiceManagerDB.getManager().getService(username, p.getIdService(), false) == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "Service doesn't exist.");
            } else if(!ServiceManagerDB.getManager().userIsOwnerOfService(p.getIdService(), username, false)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating password", "User is not owner of service.");
            } else if(!PasswordManagerDB.getManager().updatePassword(p, username)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Apis.PASSWORDS_RECOVERPASSWORD)) {
            // in the body of the request we expect to have the data
            // corresponding to the PasswordDB class, so we perform the mapping
            // using the Gson library

            if(isAdmin) {
                // input validation
                PasswordDB p = gson.fromJson(in, PasswordDB.class);

                if(p == null) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error recovering password", "Empty request body.");
                } else if(p.getIdPassword() == 0) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error recovering password", "Parameter idPassword is required.");
                } else if(!PasswordManagerDB.getManager().passwordExists(username, p.getIdPassword(), isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error recovering password", "Password not found.");
                } else if(!PasswordManagerDB.getManager().recoverPassword(p.getIdPassword(), username)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
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

                if(!PasswordManagerDB.getManager().passwordExists(username, idPwd, false)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error deleting password", "Password not found.");
                } else if(!PasswordManagerDB.getManager().userIsOwnerOfPassword(idPwd, username)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error deleting password", "User is not owner of password.");
                } else if(!PasswordManagerDB.getManager().deletePassword(username, idPwd)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error deleting password", "idPassword must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
