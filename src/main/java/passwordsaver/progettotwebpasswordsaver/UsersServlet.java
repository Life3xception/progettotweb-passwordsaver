package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Config;
import passwordsaver.progettotwebpasswordsaver.constants.Regex;
import passwordsaver.progettotwebpasswordsaver.constants.Routes;
import jakarta.servlet.annotation.WebServlet;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;
import passwordsaver.progettotwebpasswordsaver.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Users-Servlet", urlPatterns = {
        Routes.USERS,
        Routes.USERS_GETUSER,
        Routes.USERS_ADDUSER,
        Routes.USERS_UPDATEUSER,
        Routes.USERS_DELETEUSER,
        Routes.USERTYPES,
        Routes.USERTYPES_GETUSERTYPE,
        Routes.USERTYPES_ADDUSERTYPE,
        Routes.USERTYPES_UPDATEUSERTYPE,
        Routes.USERTYPES_DELETEUSERTYPE
})
public class UsersServlet extends HttpServlet {
    private Gson gson;

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.USERS)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            // retrieving all the users
            ArrayList<UserDB> users = UserManagerDB.getManager().getAllUsers();

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(users));
        } else if(request.getServletPath().equals(Routes.USERS_GETUSER)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            boolean isAdmin = loggedUser.getIdUserType() == Config.adminIdUserType;
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUser")) {
                int idUser = Integer.parseInt(pars.get("idUser")[0]);

                if(!UserManagerDB.getManager().userExists(idUser)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                } else {
                    UserDB user = UserManagerDB.getManager().getUser(idUser);

                    if(user.getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Could not get data of another user.");
                    }

                    out.println(gson.toJson(user));
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User must be provided.");
            }
        } else if(request.getServletPath().equals(Routes.USERTYPES)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            // retrieving all the usertypes
            ArrayList<UserTypeDB> userTypes = UserManagerDB.getManager().getAllUserTypes();

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(userTypes));
        } else if(request.getServletPath().equals(Routes.USERTYPES_GETUSERTYPE)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUserType")) {
                int idUserType = Integer.parseInt(pars.get("idUserType")[0]);

                if(!UserManagerDB.getManager().checkIfUserTypeExists(idUserType)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User Type not found.");
                } else {
                    UserTypeDB userType = UserManagerDB.getManager().getUserType(idUserType);
                    out.println(gson.toJson(userType));
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User Type must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.USERS_ADDUSER)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the UserDB class, so we perform the mapping
            // using the Gson library
            UserDB u = gson.fromJson(in, UserDB.class);

            // first we check if the user is admin
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(u == null) { // input validation
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(u.getEmail() == null || u.getEmail().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is required.");
            } else if(!u.getEmail().matches(Regex.EMAIL_PATTERN)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email address.");
            } else if(UserManagerDB.getManager().checkIfEmailExists(u.getIdUser(), u.getEmail())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email address already used with another account.");
            } else if(u.getUsername() == null || u.getUsername().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is required.");
            } else if(UserManagerDB.getManager().checkIfUsernameExists(u.getIdUser(), u.getUsername())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username already used.");
            } else if(u.getPassword() == null || u.getPassword().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is required.");
            } else if(!u.getPassword().matches(Regex.PASSWORD_PATTERN)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password must be from 8 to 50 " +
                        "characters long and contain at least one lowercase letter, at least one uppercase letter," +
                        " at least one digit between 0 and 9 and at least one special character in [@$!%*#?&]");
            } else if(u.getIdUserType() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User Type is required.");
            } else if(!UserManagerDB.getManager().checkIfUserTypeExists(u.getIdUserType())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User type doesn't exist.");
            } else if(UserManagerDB.getManager().addNewUser(u) > 0) {
                // retrieving the user inserted to return it to as response
                u = UserManagerDB.getManager().getUser(u.getIdUser());
                if(u != null)
                    out.println(gson.toJson(u));
                else
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error retrieving the user after inserting it.");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if(request.getServletPath().equals(Routes.USERTYPES_ADDUSERTYPE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the UserTypeDB class, so we perform the mapping
            // using the Gson library
            UserTypeDB ut = gson.fromJson(in, UserTypeDB.class);

            // first we check if the user is admin
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(ut == null) { // input validation
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(ut.getName() == null || ut.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name is required.");
            } else if(UserManagerDB.getManager().checkIfUserTypeNameExists(ut.getName())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User type already exists.");
            } else if(UserManagerDB.getManager().addNewUserType(ut) > 0) {
                // retrieving the usertype inserted to return it to as response
                ut = UserManagerDB.getManager().getUserType(ut.getIdUserType());
                if(ut != null)
                    out.println(gson.toJson(ut));
                else
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error retrieving the user type after inserting it.");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.USERS_UPDATEUSER)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the UserDB class, so we perform the mapping
            // using the Gson library
            UserDB u = gson.fromJson(in, UserDB.class);

            // input validation
            if(u == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(u.getIdUser() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User is required.");
            } else if(!UserManagerDB.getManager().userExists(u.getIdUser())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            } else if(UserManagerDB.getManager().getUserByUsername(username, true).getIdUser() != u.getIdUser()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Could not update data of another user.");
            } else if(u.getEmail() == null || u.getEmail().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is required.");
            } else if(!u.getEmail().isEmpty() && !u.getEmail().matches(Regex.EMAIL_PATTERN)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email address.");
            } else if(UserManagerDB.getManager().checkIfEmailExists(u.getIdUser(), u.getEmail())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email address already used with another account.");
            } else if(u.getUsername() == null || u.getUsername().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is required.");
            } else if(UserManagerDB.getManager().checkIfUsernameExists(u.getIdUser(), u.getUsername())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username already used.");
            } else if(u.getIdUserType() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User Type is required.");
            } else if(!UserManagerDB.getManager().checkIfUserTypeExists(u.getIdUserType())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User type doesn't exist.");
            } else if(!UserManagerDB.getManager().updateUser(u)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                // the update was successful, if the username has changed
                if(!username.equals(u.getUsername())) {
                    // we need to perform logout
                    if(!LoginService.doLogOut(request.getSession(), username)) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    // it will be the client to manage the redirect to login page
                }
            }
        } else if(request.getServletPath().equals(Routes.USERTYPES_UPDATEUSERTYPE)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the UserTypeDB class, so we perform the mapping
            // using the Gson library
            UserTypeDB ut = gson.fromJson(in, UserTypeDB.class);

            // first we check if the user is admin
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else if(ut == null) { // input validation
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(ut.getIdUserType() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User Type is required.");
            } else if(!UserManagerDB.getManager().checkIfUserTypeExists(ut.getIdUserType())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User Type not found.");
            } else if(ut.getName() == null || ut.getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name is required.");
            } else if(UserManagerDB.getManager().checkIfUserTypeNameExists(ut.getName())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User type name already used.");
            } else if(!UserManagerDB.getManager().updateUserType(ut)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals(Routes.USERS_DELETEUSER)) {
            response.setContentType("application/json");
            String username = LoginService.getCurrentLogin(request.getSession());
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(username, true);
            boolean isAdmin = loggedUser.getIdUserType() == Config.adminIdUserType;
            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUser")) {
                int idUser = Integer.parseInt(pars.get("idUser")[0]);

                if(!UserManagerDB.getManager().userExists(idUser)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                } else if(UserManagerDB.getManager().getUser(idUser).getIdUser() != loggedUser.getIdUser() && !isAdmin) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Could not delete another user.");
                } else if(!UserManagerDB.getManager().deleteUser(idUser)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } else {
                    // if the user was not admin, he deleted its account, so after removing the user
                    // we have to destroy the session
                    if(!isAdmin && !LoginService.doLogOut(request.getSession(), username))
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User must be provided.");
            }
        } else if(request.getServletPath().equals(Routes.USERTYPES_DELETEUSERTYPE)) {
            response.setContentType("application/json");
            String username = LoginService.getCurrentLogin(request.getSession());

            // only admin users can access this API
            if(!UserManagerDB.getManager().checkIfUserIsAdmin(username))
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            Map<String, String[]> pars = request.getParameterMap();

            if(pars.containsKey("idUserType")) {
                int idUserType = Integer.parseInt(pars.get("idUserType")[0]);

                if(!UserManagerDB.getManager().checkIfUserTypeExists(idUserType)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User Type not found.");
                }  else if(!UserManagerDB.getManager().deleteUserType(idUserType)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id User Type must be provided.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
