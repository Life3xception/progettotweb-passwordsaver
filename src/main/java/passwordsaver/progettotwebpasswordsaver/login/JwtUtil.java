package passwordsaver.progettotwebpasswordsaver.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "PasswordSaverSecretKey"; // FIXME: needs to be moved in a secret place
    private static final long EXPIRATION_TIME = 1000*60*30; // 30 minutes
    //86400000; // 1 day in milliseconds

    public static String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static String verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();  // returns username if token is valid
        } catch (JWTVerificationException exception) {
            return null; // Invalid token
        }
    }
}