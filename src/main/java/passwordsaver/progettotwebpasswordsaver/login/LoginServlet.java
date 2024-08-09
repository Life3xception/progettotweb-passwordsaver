package passwordsaver.progettotwebpasswordsaver.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Apis;
import passwordsaver.progettotwebpasswordsaver.model.LoginManagerDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Login-Servlet",
            urlPatterns = {Apis.LOGIN, Apis.LOGOUT})
public class LoginServlet extends HttpServlet {

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // GET accepts /login (to check login status) and /logout (to log out)

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // response variables initialization
        JsonObject result = new JsonObject();
        String operation = "";
        String username = "";
        boolean success = false;
        boolean error = true;
        String errorMessage = "";

        if(request.getServletPath().equals(Apis.LOGIN)) {
            // return the login status
            operation = "status";
            username = LoginService.getCurrentLogin(request.getSession());
            if(!username.isEmpty()) {
                success = true;
                error = false;
            } else {
                errorMessage = "No user logged in.";
            }
        } else if (request.getServletPath().equals(Apis.LOGOUT)) {
            operation = "logout";
            username = LoginService.getCurrentLogin(request.getSession());

            // check if user is logged in
            if(!username.isEmpty()) {
                if(LoginService.doLogOut(request.getSession(), username)) {
                    success = true;
                    error = false;
                } else {
                    errorMessage = "Failed to log out the user.";
                }

                username = "";
            } else {
                username = "";
                errorMessage = "User is not logged in.";
            }
        }

        // creation of response JsonObject
        result.addProperty("operation", operation);
        result.addProperty("username", username);
        result.addProperty("success", success);
        result.addProperty("error", error);
        result.addProperty("errorMessage", errorMessage);

        // return of response object
        out.println(result);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // POST accepts only /login to perform log in

        if(request.getServletPath().equals(Apis.LOGIN)) {
            response.setContentType("application/json");

            // retrieve the object passed in the POST request
            BufferedReader in = request.getReader();
            JsonObject loginObject = JsonParser.parseReader(in).getAsJsonObject();

            // retrieve information from the JsonObject
            String username = loginObject.get("username").getAsString();
            String previous = LoginService.getCurrentLogin(request.getSession());

            // preparing the object for the response
            JsonObject result = new JsonObject();
            String operation = "login";
            boolean success = false;
            boolean error = true;
            String errorMessage = "";

            // check if a user is already logged in
            if(!previous.isEmpty() && !previous.equals(username)) {
                errorMessage = "A user is already logged in. Log out before.";
            } else {
                String password = loginObject.get("password").getAsString();
                // check if password is valid
                if(LoginManagerDB.getManager().validateCredentials(username, password)) {
                    // if no user was logged in perform log in
                    if(previous.isEmpty()) {
                        if(!LoginService.doLogIn(request.getSession(), username)) {
                            errorMessage = "Failed to log in the user.";
                        }
                    }

                    // if no error occurred previously
                    if(errorMessage.isEmpty()) {
                        success = true;
                        error = false;
                    }
                } else {
                    errorMessage = "Invalid credentials.";
                }
            }

            // creation of response JsonObject
            result.addProperty("operation", operation);
            result.addProperty("username", username);
            result.addProperty("success", success);
            result.addProperty("error", error);
            result.addProperty("errorMessage", errorMessage);

            // return of response object
            response.getWriter().println(result);
        } else
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
