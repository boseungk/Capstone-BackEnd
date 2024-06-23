package com.clothz.aistyling.util;

import com.clothz.aistyling.domain.user.constant.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String username() default "test@kakao.com";
    String password() default "password1!";
    UserRole role() default UserRole.USER;
}