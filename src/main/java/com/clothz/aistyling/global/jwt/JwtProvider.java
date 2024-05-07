package com.clothz.aistyling.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private static final String ACCESS_HEADER = "Authorization";

    private static final String ACCESS_TOKEN = "AccessToken";
    private static final Long ACCESS_EXP = 1000L * 60 * 60; // 1시간
    private static final String EMAIL_CLAIM = "email";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ACCESS_SECRET = "MyAccessSecretKey1234";



    public String createAccessToken(final String email) {
        final String jwt = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(ACCESS_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public Optional<String> extractAccessToken(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_HEADER))
                .filter(accessToken -> accessToken.startsWith(TOKEN_PREFIX))
                .map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    }

    public void sendAccess(final HttpServletResponse response, final String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(ACCESS_HEADER, accessToken);
    }


    public boolean isAccessTokenValid(final String token) {
        try {
            JWT.require(Algorithm.HMAC512(ACCESS_SECRET)).build().verify(token);
            return true;
        } catch (final RuntimeException e) {
            return false;
        }
    }

    public Optional<String> verifyAccessTokenAndExtractEmail(final String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(ACCESS_SECRET))
                    .build()
                    .verify(accessToken) // accessToken 검증
                    .getClaim(EMAIL_CLAIM) // claim(Email) 가져오기
                    .asString());
        } catch (final RuntimeException e) {
            return Optional.empty();
        }
    }
}