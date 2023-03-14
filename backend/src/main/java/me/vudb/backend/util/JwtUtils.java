package me.vudb.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = "yoursfjasasdfjalskdfjlak12312sjdflkajsd93u298sdfkjgnsdkjfhgskjdfhgksjdfhgkjsdhfgkjhsdfkjghsdkjlfhgksdlkfjasdlkf1231ldfkhere";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public static String generateToken(String id, String role) {
        System.out.println(id);
        System.out.println(role);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // Check if token is expired
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                return false;
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public static String getAdminId(String token) {
        if (!validateToken(token)) {
            return null;
        }
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        if (!claims.get("role", String.class).equals("SUPER_ADMIN")) {
            return null;
        }
        return claims.getSubject();
    }

    public static String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

}
