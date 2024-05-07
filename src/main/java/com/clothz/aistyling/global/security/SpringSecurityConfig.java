package com.clothz.aistyling.global.security;

import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.global.jwt.JwtProvider;
import com.clothz.aistyling.global.jwt.filter.JwtAuthenticationFilter;
import com.clothz.aistyling.global.jwt.handler.LoginFailureHandler;
import com.clothz.aistyling.global.jwt.handler.LoginSuccessHandler;
import com.clothz.aistyling.global.jwt.service.LoginService;
import com.clothz.aistyling.global.jwt.userInfo.CustomJsonUsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SpringSecurityConfig {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final LoginService loginService;
    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        //iframe disable, h2 console 사용하기 위해
        http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        // cors 재설정
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(configurationSource()));

        //session 대신 jwt 인증 방식 사용
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        //formLogin disable
        http.formLogin(AbstractHttpConfigurer::disable);

        //커스텀 필터 적용
        http.apply(new CustomSecurityFilterManager());

        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        //로그인 인증창 뜨지 않게 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request -> request
                // /members/** URL 인증 필요
                .requestMatchers(new AntPathRequestMatcher("/members/**"))
                .authenticated()
                .anyRequest().permitAll()
        );

        return http.build();
    }

    private CorsConfigurationSource configurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        final CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(userRepository, objectMapper, jwtProvider);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationFilter(authenticationManager(), userRepository, jwtProvider);
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(final HttpSecurity builder) throws Exception {
            final AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, userRepository, jwtProvider));
            super.configure(builder);
        }
    }
}