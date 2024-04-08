package com.ou.apigateway.configs;

import java.text.ParseException;
import java.util.Date;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ou.apigateway.pojo.Account;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
    private static final String SECRECT = "ajfipupieuqwpieuasipdhfajlbfljh3y012637018274hfajlsdadfqweasdadfa3123123123123";
    private static final long HOUR = 365;
    private static final long MINUTE = 60;
    private static final long SECOND = 60;
    private static final long MILISECOND = 1000;
    private static final long EXPIRE_DURATION = HOUR * MINUTE * SECOND * MILISECOND;
    private static final byte[] BYTES = SECRECT.getBytes();

    public String generateAccessToken(Account account) {
        String token = null;
        if (account != null) {
            try {
                JWSSigner signer = new MACSigner(BYTES);
                JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
                builder.claim("email", account.getEmail());
                builder.claim("id", account.getId());
                builder.claim("roleName", account.getRoleId().getName());
                builder.issueTime(new Date(System.currentTimeMillis()));
                builder.expirationTime(new Date(System.currentTimeMillis() + EXPIRE_DURATION));

                JWTClaimsSet claimsSet = builder.build();
                SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
                signedJWT.sign(signer);

                token = signedJWT.serialize();

            } catch (JOSEException e) {
                System.out.println("[ERROR] - " + e.getMessage());
            }
        }
        return token;
    }

    private JWTClaimsSet getClaimsSet(String token) {
        JWTClaimsSet claimsSet = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(BYTES);
            if (signedJWT.verify(verifier)) {
                claimsSet = signedJWT.getJWTClaimsSet();
            }
        } catch (JOSEException | ParseException e) {
            System.out.println("[ERROR] - " + e.getMessage());
        }
        return claimsSet;
    }

    private Date getExpirationDate(String token) {
        JWTClaimsSet claimsSet = getClaimsSet(token);
        return claimsSet != null ? claimsSet.getExpirationTime() : null;
    }

    public boolean isValidAccessToken(String token) {
        if (token == null || token.trim().length() == 0) {
            return false;
        }

        Date expirationDate = getExpirationDate(token);
        String email = getEmailFromToken(token);
        Long id = getIdFromToken(token);
        log.info(String.format("Id: %d", id));
        log.info(String.format("Email: %s", email));
        // System.out.println("[DEBUG] - " + email);
        // System.out.println("[DEBUG] - " + id);
        // System.out.println("[DEBUG] - " + expirationDate);
        // System.out.println("[DEBUG] - " + !(email == null || email.isEmpty() || id ==
        // null || expirationDate.before(new Date())));
        return !(email == null || email.isEmpty() || id == null || expirationDate == null
                || expirationDate.before(new Date()));
    }

    /**
     *
     * @param token
     * @return a string with format [id,email]
     */
    public String getEmailFromToken(String token) {
        JWTClaimsSet claimsSet = getClaimsSet(token);
        String value = null;
        try {
            value = claimsSet.getStringClaim("email");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return value;
    }

    public Long getIdFromToken(String token) {
        JWTClaimsSet claimsSet = getClaimsSet(token);
        Long value = null;
        try {
            value = claimsSet.getLongClaim("id");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return value;
    }

    public String getRoleNameFromToken(String token) {
        JWTClaimsSet claimsSet = getClaimsSet(token);
        String value = null;
        try {
            value = claimsSet.getStringClaim("roleName");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return value;
    }

    public String getAuthorization(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            // CookieWebSessionIdResolver[] cookies = request.getCookies();
            HttpCookie cookie = request.getCookies().getFirst("Authorization");
            if (cookie != null && cookie.getName().equals("Authorization")) {
                header = "Bearer " + cookie.getValue();
            }
        }
        return header;
    }

    public String getAccessToken(ServerHttpRequest request) {
        String header = getAuthorization(request);
        if (header == null) {
            return null;
        }
        String token = header.split(" ")[1].trim();
        return token;
    }

    public String getAccountId(ServerHttpRequest request) {
        String token = getAccessToken(request);
        Long id = getIdFromToken(token);
        return String.valueOf(id);
    }
}
