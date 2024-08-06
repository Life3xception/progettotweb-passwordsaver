package passwordsaver.progettotwebpasswordsaver.login;

import jakarta.servlet.http.HttpSession;

public class LoginService {
    private final static String SESSION_USER_KEY = "user";

    /* Restituisce lo username dell'utente che ha fatto log in in questa sessione
     * o la stringa vuota se un utente siffatto non esiste
     */
    public static String getCurrentLogin(HttpSession session) {
        if (session.getAttribute(SESSION_USER_KEY) == null) return "";
        return (String) session.getAttribute(SESSION_USER_KEY);
    }

    /* Effettua il log in di "username" in questa sessione.
     * Ritorna true se l'operazione ha successo, false altrimenti.
     * L'operazione può fallire se in sessione risulta il log in di un altro utente.
     */
    public static boolean doLogIn(HttpSession session, String username) {
        if (session.getAttribute(SESSION_USER_KEY) == null) {
            session.setAttribute(SESSION_USER_KEY, username);
            session.setMaxInactiveInterval(10*60); // 10 minuti
            return true;
        }
        String loggedUser = (String) session.getAttribute(SESSION_USER_KEY);
        return loggedUser.equals(username);
    }

    /* Effettua il log out di "username" in questa sessione.
     * Ritorna true se l'operazione ha successo, false altrimenti.
     * L'operazione può fallire se in sessione risulta il log in di un altro utente.
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