package com.example.whiskervet.security;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JWTGenerator {
    
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Long EXPIRATION_TIME = 7000000L;

    // Set para almacenar tokens revocados
    private static final Set<String> revokedTokens = new HashSet<>();

    /*Crear el JWT */
    public String generateToken(Authentication authentication) {
        /*Datos necesarios para la creación */
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + EXPIRATION_TIME);

        /*Crear el JWT */
        String token = Jwts.builder().setSubject(username)
        .setIssuedAt(currentDate)
        .setExpiration(expireDate)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
        return token;
    }

    /*Metodo para obtener el usuario a partir del JWT */
    public String getUserFromJwt(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /* Método para validar el JWT */
    public boolean validateToken(String token){
        try {
            // Verificar si el token está en la lista de tokens revocados
            if (revokedTokens.contains(token)) {
                return false;
            }

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* Método para revocar un token (logout) */
    public void revokeToken(String token) {
        // Agregar el token a la lista de tokens revocados
        revokedTokens.add(token);
    }

}