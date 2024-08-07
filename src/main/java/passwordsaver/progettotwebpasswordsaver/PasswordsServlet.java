package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Regex;
import passwordsaver.progettotwebpasswordsaver.constants.Routes;
import passwordsaver.progettotwebpasswordsaver.model.PasswordDB;
import passwordsaver.progettotwebpasswordsaver.model.PasswordManagerDB;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;
import passwordsaver.progettotwebpasswordsaver.model.ServiceManagerDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "Passwords-Servlet",
            urlPatterns = {
                    Routes.PASSWORDS,
                    Routes.PASSWORDS_ADDPASSWORD
            })
public class PasswordsServlet extends HttpServlet {
    private Gson gson;

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.PASSWORDS)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // TODO: potrebbe servire per richiesta password per tipo servizio
            // retrieving the parameters from the querystring as a key-value Map
            // Map<String, String[]> params = request.getParameterMap();

            // retrieving all the valid passwords for the user
            ArrayList<PasswordDB> passwords = PasswordManagerDB.getManager().getAllPasswords(username);

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(passwords));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.PASSWORDS_ADDPASSWORD)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the PasswordDB class, so we perform the mapping
            // using the Gson library
            PasswordDB p = gson.fromJson(in, PasswordDB.class);

            // input validation
            if(p == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty request body.");
            } else if(p.getPassword().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is required.");
            } else if(!p.getEmail().matches(Regex.EMAIL_PATTERN)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email address.");
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
}
