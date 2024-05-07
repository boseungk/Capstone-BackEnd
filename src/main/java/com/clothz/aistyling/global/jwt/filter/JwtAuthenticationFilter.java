package com.clothz.aistyling.global.jwt.filter;

import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.global.jwt.JwtProvider;
import com.clothz.aistyling.global.jwt.userInfo.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final String NO_CHECK_URL = "/api/login";
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager, final UserRepository userRepository, final JwtProvider jwtProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        // "/api/login" 요청은 토큰 확인 x
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            chain.doFilter(request, response);
            return;
        }
        // refresh token 만료 시 /token/refresh 에서 요청
        checkAccessTokenAndAuthentication(request, response, chain);

    }

    private void checkAccessTokenAndAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        jwtProvider.extractAccessToken(request)
                .filter(jwtProvider::isAccessTokenValid)
                .ifPresent(accessToken -> jwtProvider.verifyAccessTokenAndExtractEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(final User myUser) {
        String password = myUser.getPassword();

        final UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getUserRole().getType())
                .build();

        final UserDetails customUserDetailsUser = CustomUserDetails.from(myUser);

        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetailsUser,
                        customUserDetailsUser.getPassword(),
                        userDetailsUser.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
