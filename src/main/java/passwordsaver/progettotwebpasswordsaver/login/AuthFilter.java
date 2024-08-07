package passwordsaver.progettotwebpasswordsaver.login;

import passwordsaver.progettotwebpasswordsaver.constants.Routes;

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
        if (req.getServletPath().equals(Routes.LOGIN) ||
                req.getServletPath().equals(Routes.LOGOUT)) {
            chain.doFilter(req, res);
            return;
        }

        // check if the user is logged in
        if (!LoginService.getCurrentLogin(req.getSession()).isEmpty()) {
            chain.doFilter(req, res);
            return;
        }

        // if the urls are not LOGIN nor LOGOUT and the user is not logged in
        // we return 401 and STOP the chain filter (otherwise we get errors,
        // because we already send an error in the response!!!)
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        //chain.doFilter(req, res);
    }
}
