package com.example.adminapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtService {

  private static final String ROLE_CLAIM = "role";

  @Value("${security.jwt.secret}")
  private String secret;

  @Value("${security.jwt.expiration}")
  private Duration expiration;

  private SecretKey signingKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username, String role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expiration.toMillis());

    return Jwts.builder()
      .setSubject(username)
      .claim(ROLE_CLAIM, role)
      .setIssuedAt(now)
      .setExpiration(expiry)
      .signWith(signingKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(signingKey())
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Optional<String> extractUsername(String token) {
    return Optional.ofNullable(getClaims(token).getSubject());
  }

  public Optional<String> extractRole(String token) {
    return Optional.ofNullable(getClaims(token).get(ROLE_CLAIM, String.class));
  }

  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);
    String username = claims.getSubject();
    String role = claims.get(ROLE_CLAIM, String.class);
    List<SimpleGrantedAuthority> authorities =
      role != null ? List.of(new SimpleGrantedAuthority("ROLE_" + role)) : Collections.emptyList();

    return new UsernamePasswordAuthenticationToken(username, null, authorities);
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(signingKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }
}
