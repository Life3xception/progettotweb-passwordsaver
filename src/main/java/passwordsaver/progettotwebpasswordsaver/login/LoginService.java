package passwordsaver.progettotwebpasswordsaver.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoginService {
    private final static String SESSION_USER_KEY = "user";

    /*
     *  Returns the username of the logged in user for the passed session
     *  or empty string if no user is logged in
     */
    /*public static String getCurrentLogin(HttpSession session) {
        if (session.getAttribute(SESSION_USER_KEY) == null) return "";
        return (String) session.getAttribute(SESSION_USER_KEY);
    }*/

    public static String getCurrentLogin(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);  // Remove "Bearer " prefix

            return JwtUtil.verifyToken(token);
        }

        return null;
    }

    /*
     * Performs the log in for the given user in this session. Returns true
     * if the operation is successful, false otherwise.
     * It could fail if another user is logged in for the given session.
     */
    public static boolean doLogIn(HttpSession session, String username) {
        if (session.getAttribute(SESSION_USER_KEY) == null) {
            session.setAttribute(SESSION_USER_KEY, username);
            session.setMaxInactiveInterval(10 * 60); // 10 minuti
            return true;
        }
        String loggedUser = (String) session.getAttribute(SESSION_USER_KEY);
        return loggedUser.equals(username);
    }

    /*
     * Performs the log out of the give user in this session.
     * Returns true if the operation is successful, false otherwise.
     * It could fail if another user is logged in for the given session.
     */
    public static boolean doLogOut(HttpSession session, String username) {
        if (session.getAttribute(SESSION_USER_KEY) == null) {
            return true;
        }
        if (((String) session.getAttribute(SESSION_USER_KEY)).equals(username)) {
            session.invalidate();
            return true;
        }
        return false;
    }
}