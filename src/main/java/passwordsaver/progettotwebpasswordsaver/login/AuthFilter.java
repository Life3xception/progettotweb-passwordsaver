package passwordsaver.progettotwebpasswordsaver.login;

import passwordsaver.progettotwebpasswordsaver.constants.Apis;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "*")
public class AuthFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        // check if the user is logged in
        if (LoginService.getCurrentLogin(req) != null) {
            chain.doFilter(req, res);
            return;
        }

        // check if the route requested is the login (doesn't require to be logged in)
        if (req.getServletPath().startsWith(Apis.LOGIN)) {
            chain.doFilter(req, res);
            return;
        }

        // if the route is not login and the user is not logged in
        // we return 401 and STOP the chain filter (otherwise we get errors,
        // because we already send an error in the response!!!)
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
