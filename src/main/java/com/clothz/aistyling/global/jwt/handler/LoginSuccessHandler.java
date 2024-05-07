package com.clothz.aistyling.global.jwt.handler;


import com.clothz.aistyling.api.ApiResponse;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.global.jwt.JwtProvider;
import com.clothz.aistyling.global.jwt.dto.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final String email = extractUsername(authentication);
        final String accessToken = jwtProvider.createAccessToken(email);

        jwtProvider.sendAccess(response, accessToken);

        final User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email)
        );
        userRepository.saveAndFlush(user);

        final var responseDTO = LoginResponse.from(user.getEmail());
        createResponse(response, responseDTO);
    }

    private String extractUsername(final Authentication authentication) {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private void createResponse(final HttpServletResponse response, final LoginResponse responseDTO) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        final String result = objectMapper.writeValueAsString(ApiResponse.ok(responseDTO));
        response.getWriter().write(result);
    }
}
