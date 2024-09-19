package passwordsaver.progettotwebpasswordsaver.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Apis;
import passwordsaver.progettotwebpasswordsaver.constants.Config;
import passwordsaver.progettotwebpasswordsaver.model.*;
import passwordsaver.progettotwebpasswordsaver.utils.JsonErrorResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Login-Servlet", urlPatterns = { Apis.LOGIN, Apis.SIGNUP })
public class LoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // GET accepts /login (to check login status)
        if(request.getServletPath().equals(Apis.LOGIN)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            // response variables initialization
            JsonObject result = new JsonObject();
            String operation = "status";
            boolean success = false;
            boolean error = true;
            String errorMessage = "";

            // return the login status
            String username = LoginService.getCurrentLogin(request);
            if (username != null) {
                success = true;
                error = false;
            } else {
                errorMessage = "Not logged in.";
            }

            // creation of response JsonObject
            result.addProperty("operation", operation);
            result.addProperty("username", username);
            result.addProperty("success", success);
            result.addProperty("error", error);
            result.addProperty("errorMessage", errorMessage);

            // return of response object
            out.println(result);
        } else
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // POST accepts /login to perform log in and /login/signup to perform signup

        if(request.getServletPath().equals(Apis.LOGIN)) {
            response.setContentType("application/json");
            Gson gson = new Gson();

            // retrieve the object passed in the POST request
            BufferedReader in = request.getReader();
            JsonObject loginObject = JsonParser.parseReader(in).getAsJsonObject();

            // retrieve information from the JsonObject
            String username = loginObject.get("username").getAsString();

            // preparing the object for the response
            JsonObject result = new JsonObject();
            String operation = "login";
            boolean success = false;
            boolean error = true;
            String errorMessage = "";
            String token = "";
            String userType = "";

            // check if a user is already logged in (if null, no JWT in request)
            if(LoginService.getCurrentLogin(request) != null) {
                errorMessage = "A user is already logged in. Log out before.";
            } else {
                String password = loginObject.get("password").getAsString();
                // check if password is valid
                if(LoginManagerDB.getManager().validateCredentials(username, password)) {
                    userType = gson.toJson(UserManagerDB.getManager().getUserTypeOfUser(username));
                    token = JwtUtil.createToken(username, userType);
                    success = true;
                    error = false;
                } else {
                    errorMessage = "Invalid credentials.";
                }
            }

            // creation of response JsonObject
            result.addProperty("operation", operation);
            result.addProperty("success", success);
            result.addProperty("error", error);
            result.addProperty("errorMessage", errorMessage);

            // creating login data object only on successful login
            if(success) {
                JsonObject loginData = new JsonObject();
                loginData.addProperty("username", username);
                loginData.addProperty("idUser", UserManagerDB.getManager().getUserByUsername(username, false).getIdUser());
                loginData.addProperty("token", token);
                loginData.addProperty("userType", userType);
                result.add("loginData", loginData);
            }

            // return of response object
            response.getWriter().println(result);
        } else if(request.getServletPath().equals(Apis.SIGNUP)) {
            response.setContentType("application/json");
            Gson gson = new Gson();
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            
            // in the body of the request we expect to have the data
            // corresponding to the UserDB class, so we perform the mapping
            // using the Gson library
            UserDB u = gson.fromJson(in, UserDB.class);

            if(u == null) { // input validation
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Empty request body.");
            } else if(u.getEmail() == null || u.getEmail().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Parameter email is required.");
            } else if(!u.getEmail().matches(Config.EMAIL_PATTERN)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Invalid email address.");
            } else if(UserManagerDB.getManager().checkIfEmailExists("not-logged-user", u.getIdUser(), u.getEmail())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Email address already used with another account.");
            } else if(u.getUsername() == null || u.getUsername().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Parameter username is required.");
            } else if(UserManagerDB.getManager().checkIfUsernameExists(u.getIdUser(), u.getUsername())) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Username already used.");
            } else if(u.getPassword() == null || u.getPassword().isEmpty()) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Parameter password is required.");
            } else if(!u.getPassword().matches(Config.PASSWORD_PATTERN)) {
                JsonErrorResponse.sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Error during signup", "Password must be from 8 to 50 " +
                        "characters long and contain at least one lowercase letter, at least one uppercase letter," +
                        " at least one digit between 0 and 9 and at least one special character in [@$!%*#?&]");
            } else if(UserManagerDB.getManager().addNewUser("not-logged-user", u, false) <= 0) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                // creation of response JsonObject
                JsonObject result = new JsonObject();
                result.addProperty("operation", "signup");
                result.addProperty("success", true);
                result.addProperty("error", false);
                result.addProperty("errorMessage", "");

                // return of response object
                out.println(result);
            }
        } else
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
