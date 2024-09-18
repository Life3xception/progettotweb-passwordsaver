package passwordsaver.progettotwebpasswordsaver.login;

import jakarta.servlet.http.HttpServletRequest;

public class LoginService {

    /*
     * Returns the username of the user if a valid Bearer JWT token is included
     * in the headers of the request, null otherwise
     */
    public static String getCurrentLogin(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);  // Remove "Bearer " prefix

            return JwtUtil.verifyToken(token);
        }

        return null;
    }
}