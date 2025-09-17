package tvz.jwafp.api.auth;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class JwtGenerator {
    private static final String SECRET = "c3VwZXJic2VjcmVldGp3dGdlbmVyZXRpb25rZXlzdHRyaW5ndGhldHN2ZXJ5bG9uZ2FuZGNvbnZvbHV0ZWRhbmRoYXN0eXBvcw==";

    public static JwToken makeToken(String username, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        return new JwToken(generateAccessToken(username, now, expiryDate), username, expiryDate);
    }

    private static String generateAccessToken(String username, Date issueDate, Date expiryDate) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(issueDate)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private static SecretKey getSigningKey() {
        return new SecretKeySpec(Base64.getDecoder()
                .decode(SECRET.getBytes(StandardCharsets.UTF_8)), "HmacSHA256");
    }
}
