package com.intuit.craft.booking.service;

import com.intuit.craft.booking.domain.BookingLockRequest;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class TokenService {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final String tokenSecretKey;
    private final long tokenExpiryDurationInSeconds;

    public TokenService(@Value("${booking.secret.key") String tokenSecretKey,
                        @Value("${booking.secret.token-expiry-time-in-seconds}") long tokenExpiryDurationInSeconds) {
        this.tokenSecretKey = tokenSecretKey;
        this.tokenExpiryDurationInSeconds = tokenExpiryDurationInSeconds;
    }

    public String generateToken(String transactionId, BookingLockRequest bookingLockRequest) {
        log.info("Generating token for granted request");
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenSecretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        long nowInMilliSeconds = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder().setId(transactionId)
                .setIssuedAt(new Date(nowInMilliSeconds))
                .setSubject("lock-acquired")
                .setIssuer(bookingLockRequest.getEventId())
                .signWith(signatureAlgorithm, signingKey);

        Date exp = new Date((tokenExpiryDurationInSeconds * 1000) + nowInMilliSeconds);
        builder.setExpiration(exp);
        return builder.compact();
    }
}
