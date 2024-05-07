package com.clothz.aistyling.global.jwt.service;

import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.global.jwt.JwtProvider;
import com.clothz.aistyling.global.jwt.userInfo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return CustomUserDetails.from(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))
        );
    }
}
