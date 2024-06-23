package com.clothz.aistyling.util;

import com.clothz.aistyling.global.jwt.userInfo.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(final WithMockCustomUser annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        final UserDetails userDetail = User.builder().username(annotation.username()).password(annotation.password()).roles(annotation.role().name()).build();
        final CustomUserDetails customUserDetails = CustomUserDetails.builder().id(annotation.id()).email(annotation.username()).password(annotation.password()).role(annotation.role()).build();
        final Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetails, "", userDetail.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
