package com.electronic.store.ElectronicStore.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper
{
    public static final long TOKEN_VALIDITY=5*60*1000;
    public static final String SECRET_KEY="AR3uGqF9Cks1lNgF4QgXKvcVw6i1peLMHHhSTwI3Wh7DK6xGSJlXc1VXyNQjFEX+Bl4S9YXREuSjDRd8ynOVOg==\n";

    //This is for retrieving the username for jwt token
    public String getUserNameFromToken(String token)
    {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T>claimsResolver)
    {
        final Claims claims=getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token)
    {

        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getPayload();
    }

    public Boolean isTokenExpired(String token)
    {
        final Date expiration=getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token)
    {
        return getClaimFromToken(token,Claims::getExpiration);
    }


    //Now we will generate the token
    public String generateToken(UserDetails userDetails)
    {
        Map<String,Object> claims=new HashMap<>();
        return doGenerateTokens(claims,userDetails.getUsername());
    }

    private String doGenerateTokens(Map<String, Object> claims, String subject)
    {
        return Jwts.builder()
                .setClaims(claims).
                setSubject(subject).
                setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY).compact();
    }



}
