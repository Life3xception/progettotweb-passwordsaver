package passwordsaver.progettotwebpasswordsaver.db;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    private static BCryptPasswordEncoder encoder;

    public static BCryptPasswordEncoder getPasswordEncoder() {
        if(encoder == null) {
            encoder = new BCryptPasswordEncoder();
        }
        return encoder;
    }
}
