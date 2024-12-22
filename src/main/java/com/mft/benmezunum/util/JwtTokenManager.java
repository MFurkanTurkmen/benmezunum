package com.mft.benmezunum.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mft.benmezunum.exception.AllExceptions;
import com.mft.benmezunum.exception.customException.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {

    @Value("${jwt.secret}")
    private String sifreAnahtari ;

    public Optional<String> createToken(String username){
        String token;

        Long exDate = 1000L*60*60*24;// 1 gün
        try{

            token =  JWT.create()
                    .withAudience()
                    .withClaim("username",username)
                    .withClaim("isOne",true)
                    .withIssuer("mft")
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis()+exDate))
                    .sign(Algorithm.HMAC512(sifreAnahtari));
            return Optional.of(token);
        }catch (Exception ex){
            throw new AuthenticationException(AllExceptions.UNKNOWN_ERROR,"Token oluşturulamadı.");
        }
    }

    public Boolean validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(sifreAnahtari);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("mft")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT==null)
                return false;
        }catch (Exception exception){
            throw new AuthenticationException(AllExceptions.UNKNOWN_ERROR);
        }
        return true;
    }

    public Optional<String> getUsernameFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(sifreAnahtari);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("mft")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT==null)
                return Optional.empty();
            return Optional.of(decodedJWT.getClaim("username").asString());
        }catch (Exception exception){
            throw new AuthenticationException(AllExceptions.UNKNOWN_ERROR);
        }

    }


}
