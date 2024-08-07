package passwordsaver.progettotwebpasswordsaver.encryption;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptEncoder {
    private static BCryptPasswordEncoder encoder;

    public static BCryptPasswordEncoder getPasswordEncoder() {
        if(encoder == null) {
            encoder = new BCryptPasswordEncoder();
        }
        return encoder;
    }
}
