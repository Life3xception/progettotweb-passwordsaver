package passwordsaver.progettotwebpasswordsaver;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import passwordsaver.progettotwebpasswordsaver.constants.Routes;
import jakarta.servlet.annotation.WebServlet;
import passwordsaver.progettotwebpasswordsaver.login.LoginService;
import passwordsaver.progettotwebpasswordsaver.model.UserDB;
import passwordsaver.progettotwebpasswordsaver.model.UserManagerDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "Users-Servlet",
            urlPatterns = {
                Routes.USERS,
                Routes.USERS_UPDATEUSER
            })
public class UsersServlet extends HttpServlet {
    private Gson gson;

    public void init() { gson = new Gson(); }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.USERS)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // retrieving all the users
            ArrayList<UserDB> users = UserManagerDB.getManager().getAllUsers();

            // returning the arraylist as an array of JsonObject using the Gson library
            out.println(gson.toJson(users));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getServletPath().equals(Routes.USERS_UPDATEUSER)) {
            response.setContentType("application/json");
            BufferedReader in = request.getReader();
            PrintWriter out = response.getWriter();
            String username = LoginService.getCurrentLogin(request.getSession());

            // in the body of the request we expect to have the data
            // corresponding to the UserDB class, so we perform the mapping
            // using the Gson library
            UserDB u = gson.fromJson(in, UserDB.class);

            // input validation
            // TODO

            UserManagerDB.getManager().updateUser(u);

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
