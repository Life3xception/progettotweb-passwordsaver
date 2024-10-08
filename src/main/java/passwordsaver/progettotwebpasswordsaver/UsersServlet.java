package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Config;
import passwordsaver.progettotwebpasswordsaver.constants.Apis;
import jakarta.servlet.annotation.WebServlet;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;
import passwordsaver.progettotwebpasswordsaver.model.*;
import passwordsaver.progettotwebpasswordsaver.utils.JsonErrorResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Users-Servlet", urlPatterns = {
        Apis.USERS,
        Apis.USERS_GETDETAILEDUSERS,
        Apis.USERS_GETDETAILEDUSERSBYUSERTYPE,
        Apis.USERS_GETUSER,
        Apis.USERS_GETDETAILEDUSER,
        Apis.USERS_GETUSERTYPEOFUSER,
        Apis.USERS_ADDUSER,
        Apis.USERS_UPDATEUSER,
        Apis.USERS_CHANGEPASSWORD,
        Apis.USERS_DELETEUSER,
        Apis.USERTYPES,
        Apis.USERTYPES_GETUSERTYPE,
        Apis.USERTYPES_ADDUSERTYPE,
        Apis.USERTYPES_UPDATEUSERTYPE,
        Apis.USERTYPES_DELETEUSERTYPE
})
public class UsersServlet extends HttpServlet {
    private Gson gson;

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = LoginService.getCurrentLogin(request);
        boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if(request.getServletPath().equals(Apis.USERS)) {
            // only admin users can access this API
            if (!isAdmin)
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            // retrieving all the users
            ArrayList<UserDB> users = UserManagerDB.getManager().getAllUsers(username, isAdmin);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(users));
        } else if(request.getServletPath().equals(Apis.USERS_GETDETAILEDUSERS)) {
            // only admin users can access this API
            if(!isAdmin)
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            // retrieving all the users
            ArrayList<DetailedUserDB> users = UserManagerDB.getManager().getAllDetailedUsers(username, isAdmin);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(users));
        } else if(request.getServletPath().equals(Apis.USERS_GETDETAILEDUSERSBYUSERTYPE)) {
            Map<String, String[]> pars = request.getParameterMap();

            // only admin users can access this API
            if(!isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(pars.containsKey("idUserType")) {
                int idUserType = Integer.parseInt(pars.get("idUserType")[0]);

                if(!UserManagerDB.getManager().checkIfUserTypeExists(username, idUserType, isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting user", "User Type not found.");
                } else {
                    ArrayList<DetailedUserDB> users = UserManagerDB.getManager().getAllDetailedUsersByUserType(username, idUserType, isAdmin);
                    out.println(gson.toJson(users));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting user", "idUserType must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.USERS_GETUSER)) {
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUser")) {
                int idUser = Integer.parseInt(pars.get("idUser")[0]);

                if(!UserManagerDB.getManager().userExists(username, idUser, isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting user", "User not found.");
                } else {
                    UserDB user = UserManagerDB.getManager().getUser(username, idUser, isAdmin);

                    if(user.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error getting user", "Could not get data of another user.");
                    }

                    out.println(gson.toJson(user));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting user", "idUser must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.USERS_GETDETAILEDUSER)) {
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUser")) {
                int idUser = Integer.parseInt(pars.get("idUser")[0]);

                if(!UserManagerDB.getManager().userExists(username, idUser, isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting user", "User not found.");
                } else {
                    DetailedUserDB user = UserManagerDB.getManager().getDetailedUser(username, idUser, isAdmin);

                    if(user.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error getting user", "Could not get data of another user.");
                    }

                    out.println(gson.toJson(user));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting user", "idUser must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.USERS_GETUSERTYPEOFUSER)) {
            UserTypeDB res = UserManagerDB.getManager().getUserTypeOfUser(username);
            out.println(gson.toJson(res));
        } else if(request.getServletPath().equals(Apis.USERTYPES)) {
            // only admin users can access this API
            if(!isAdmin)
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            // retrieving all the usertypes
            ArrayList<UserTypeDB> userTypes = UserManagerDB.getManager().getAllUserTypes(username, isAdmin);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(userTypes));
        } else if(request.getServletPath().equals(Apis.USERTYPES_GETUSERTYPE)) {
            // only admin users can access this API
            if(!isAdmin)
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUserType")) {
                int idUserType = Integer.parseInt(pars.get("idUserType")[0]);

                if(!UserManagerDB.getManager().checkIfUserTypeExists(username, idUserType, isAdmin)) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error getting user type", "User Type not found.");
                } else {
                    UserTypeDB userType = UserManagerDB.getManager().getUserType(username, idUserType, isAdmin);
                    out.println(gson.toJson(userType));
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error getting user type", "idUserType must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BufferedReader in = request.getReader();
        PrintWriter out = response.getWriter();
        String username = LoginService.getCurrentLogin(request);
        boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

        if(request.getServletPath().equals(Apis.USERS_ADDUSER)) {
            // in the body of the request we expect to have the data
            // corresponding to the UserDB class, so we perform the mapping
            // using the Gson library
            UserDB u = gson.fromJson(in, UserDB.class);

            // first we check if the user is admin
            if(!isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(u == null) { // input validation
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Empty request body.");
            } else if(u.getEmail() == null || u.getEmail().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Parameter email is required.");
            } else if(!u.getEmail().matches(Config.EMAIL_PATTERN)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Invalid email address.");
            } else if(UserManagerDB.getManager().checkIfEmailExists(username, u.getIdUser(), u.getEmail())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Email address already used with another account.");
            } else if(u.getUsername() == null || u.getUsername().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Parameter username is required.");
            } else if(UserManagerDB.getManager().checkIfUsernameExists(u.getIdUser(), u.getUsername())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Username already used.");
            } else if(u.getPassword() == null || u.getPassword().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Parameter password is required.");
            } else if(!u.getPassword().matches(Config.PASSWORD_PATTERN)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Password must be from 8 to 50 " +
                        "characters long and contain at least one lowercase letter, at least one uppercase letter," +
                        " at least one digit between 0 and 9 and at least one special character in [@$!%*#?&]");
            } else if(u.getIdUserType() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "Parameter idUserType is required.");
            } else if(!UserManagerDB.getManager().checkIfUserTypeExists(username, u.getIdUserType(), isAdmin)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user", "User type doesn't exist.");
            } else if(UserManagerDB.getManager().addNewUser(username, u, isAdmin) > 0) {
                // retrieving the user inserted to return it to as response
                u = UserManagerDB.getManager().getUser(username, u.getIdUser(), isAdmin);
                if(u != null)
                    out.println(gson.toJson(u));
                else
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error adding user", "Error retrieving the user after inserting it.");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Apis.USERTYPES_ADDUSERTYPE)) {
            // in the body of the request we expect to have the data
            // corresponding to the UserTypeDB class, so we perform the mapping
            // using the Gson library
            UserTypeDB ut = gson.fromJson(in, UserTypeDB.class);

            // first we check if the user is admin
            if(!isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(ut == null) { // input validation
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user type", "Empty request body.");
            } else if(ut.getName() == null || ut.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user type", "Parameter name is required.");
            } else if(UserManagerDB.getManager().checkIfUserTypeNameExists(username, ut.getName(), 0)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding user type", "User type already exists.");
            } else if(UserManagerDB.getManager().addNewUserType(username, ut, isAdmin) > 0) {
                // retrieving the usertype inserted to return it to as response
                ut = UserManagerDB.getManager().getUserType(username, ut.getIdUserType(), isAdmin);
                if(ut != null)
                    out.println(gson.toJson(ut));
                else
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error adding user type", "Error retrieving the user type after inserting it.");
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
        UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
        boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

        if(request.getServletPath().equals(Apis.USERS_UPDATEUSER)) {
            // in the body of the request we expect to have the data
            // corresponding to the UserDB class, so we perform the mapping
            // using the Gson library
            UserDB u = gson.fromJson(in, UserDB.class);

            // input validation
            if(u == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Empty request body.");
            } else if(u.getIdUser() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Parameter idUser is required.");
            } else if(!UserManagerDB.getManager().userExists(username, u.getIdUser(), isAdmin)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error updating user", "User not found.");
            } else if(!isAdmin && UserManagerDB.getManager().getUserByUsername(username, true).getIdUser() != u.getIdUser()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error updating user", "Could not update data of another user.");
            } else if(u.getEmail() == null || u.getEmail().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Parameter email is required.");
            } else if(!u.getEmail().matches(Config.EMAIL_PATTERN)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Invalid email address.");
            } else if(UserManagerDB.getManager().checkIfEmailExists(username, u.getIdUser(), u.getEmail())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Email address already used with another account.");
            } else if(u.getUsername() == null || u.getUsername().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Parameter username is required.");
            } else if(UserManagerDB.getManager().checkIfUsernameExists(u.getIdUser(), u.getUsername())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Username already used.");
            } else if(u.getIdUserType() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "Parameter idUserType is required.");
            } else if(!UserManagerDB.getManager().checkIfUserTypeExists(username, u.getIdUserType(), isAdmin)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user", "User type doesn't exist.");
            } else if(!UserManagerDB.getManager().updateUser(username, u, isAdmin)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                // the update was successful, if the username has changed and the user is the same as the logged one
                if(loggedUser.getIdUser() == u.getIdUser() && !username.equals(u.getUsername())) {
                    // we need to tell the fe to perform logout!
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Username has changed. Please log in again");
                }
            }
        } else if(request.getServletPath().equals(Apis.USERS_CHANGEPASSWORD)) {
            // in the body of the request we expect to have the data
            // corresponding to the UserDB class, so we perform the mapping
            // using the Gson library
            UserDB u = gson.fromJson(in, UserDB.class);

            // input validation
            if (u == null) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error changing password", "Empty request body.");
            } else if (u.getIdUser() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error changing password", "Parameter idUser is required.");
            } else if (!UserManagerDB.getManager().userExists(username, u.getIdUser(), false)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error changing password", "User not found.");
            } else if (UserManagerDB.getManager().getUserByUsername(username, true).getIdUser() != u.getIdUser()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error changing password", "Could not change password of another user.");
            } else if (!UserManagerDB.getManager().changePassword(username, u)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Apis.USERTYPES_UPDATEUSERTYPE)) {
            // in the body of the request we expect to have the data
            // corresponding to the UserTypeDB class, so we perform the mapping
            // using the Gson library
            UserTypeDB ut = gson.fromJson(in, UserTypeDB.class);

            // first we check if the user is admin
            if(!isAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(ut == null) { // input validation
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user type", "Empty request body.");
            } else if(ut.getIdUserType() == 0) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user type", "Parameter idUserType is required.");
            } else if(!UserManagerDB.getManager().checkIfUserTypeExists(username, ut.getIdUserType(), isAdmin)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error updating user type", "User Type not found.");
            } else if(ut.getName() == null || ut.getName().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user type", "Parameter name is required.");
            } else if(UserManagerDB.getManager().checkIfUserTypeNameExists(username, ut.getName(), ut.getIdUserType())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating user type", "User type name already used.");
            } else if(!UserManagerDB.getManager().updateUserType(username, ut, isAdmin)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String username = LoginService.getCurrentLogin(request);
        boolean isAdmin = UserManagerDB.getManager().checkIfUserIsAdmin(username);

        if (request.getServletPath().equals(Apis.USERS_DELETEUSER)) {
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUser")) {
                int idUser = Integer.parseInt(pars.get("idUser")[0]);

                if(!UserManagerDB.getManager().userExists(username, idUser, false)) { // se un utente è già eliminato, non ha senso eliminarlo!
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error deleting user", "User not found.");
                } else if(UserManagerDB.getManager().getUser(username, idUser, isAdmin).getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Error deleting user", "Could not delete another user.");
                } else if(!UserManagerDB.getManager().deleteUser(username, idUser)) { // TODO: devo eliminare anche tutte le password e tutti i services dell'utente
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } else {
                    // if the user was not admin, he deleted its account, so after removing the user it is no longer authorized
                    if(!isAdmin)
                        JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Account deleted");
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error deleting user", "idUser must be provided.");
            }
        } else if(request.getServletPath().equals(Apis.USERTYPES_DELETEUSERTYPE)) {
            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUserType")) {
                int idUserType = Integer.parseInt(pars.get("idUserType")[0]);

                if(!UserManagerDB.getManager().checkIfUserTypeExists(username, idUserType, false)) { // Non serve eliminare un usertype già eliminato
                    JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Error deleting user type", "User Type not found.");
                }  else if(!UserManagerDB.getManager().deleteUserType(username, idUserType)) { // TODO: devo eliminare anche tutti gli utenti, o switchare ad un altro usertype
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error deleting user type", "idUserType must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
