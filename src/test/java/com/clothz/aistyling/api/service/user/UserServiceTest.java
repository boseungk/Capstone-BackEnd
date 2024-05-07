package com.clothz.aistyling.api.service.user;

import com.clothz.aistyling.api.controller.user.request.UserCreateRequest;
import com.clothz.aistyling.api.controller.user.request.UserUpdateRequest;
import com.clothz.aistyling.api.service.user.response.UserInfoResponse;
import com.clothz.aistyling.api.service.user.response.UserSingUpResponse;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.domain.user.constant.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {
    private static final String EMAIL = "user12@gmail.com";
    private static final String SAME_EMAIL = "user12@gmail.com";
    private static final String ANOTHER_EMAIL = "user34@gmail.com";
    private static final String NICKNAME = "user";
    private static final String PASSWORD = "password1!";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @BeforeEach
    void beforeEach() {
        final User user = User.builder()
                .nickname(NICKNAME)
                .email(EMAIL)
                .password(delegatingPasswordEncoder.encode(PASSWORD))
                .userRole(UserRole.USER)
                .userImages("[\"image1.png\", \"image2.png\"]")
                .build();
        userRepository.save(user);
        clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("회원 가입을 한다.")
    @Test
    void signUp() throws JsonProcessingException {
        //given
        final UserCreateRequest request = UserCreateRequest.builder()
                .nickname(NICKNAME)
                .email(ANOTHER_EMAIL)
                .password(PASSWORD)
                .build();
        final var images = List.of("image1.png", "images2.png");

        //when
        final UserSingUpResponse userSingUpResponse = userService.signUp(request);

        //then
        assertThat(userSingUpResponse.email()).isEqualTo(ANOTHER_EMAIL);
        assertThat(userSingUpResponse.nickname()).isEqualTo(NICKNAME);
    }

    @DisplayName("회원 가입을 할 때 다른 이메일이어야 한다.")
    @Test
    void signUpWithNoDuplicateEmail() {
        //given
        final UserCreateRequest request = UserCreateRequest.builder()
                .nickname(NICKNAME)
                .email(SAME_EMAIL)
                .password(PASSWORD)
                .build();

        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    userService.signUp(request);
                }).isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("Email already exists");
    }

    @DisplayName("사용자의 이미지를 업로드 한다.")
    @Test
    void uploadUserImg() throws IOException {
        //given
        final User user = userRepository.findByEmail(EMAIL).orElseThrow();
        final var images = List.of("image1.png", "images2.png");

        //when
        final var userImagesResponse = userService.uploadUserImg(images, user.getId());

        //then
        assertThat(userImagesResponse.imgUrls())
                .hasSize(2)
                .containsExactlyInAnyOrder("image1.png", "images2.png");
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getUserInfo() throws JsonProcessingException {
        //given
        final User user = userRepository.findByEmail(EMAIL).orElseThrow();

        //when
        final UserInfoResponse userInfoResponse = userService.getUserInfo(user.getId());

        //then
        assertThat(userInfoResponse.email()).isEqualTo(EMAIL);
        assertThat(userInfoResponse.nickname()).isEqualTo(NICKNAME);
        assertThat(userInfoResponse.images())
                .hasSize(2)
                .containsExactlyInAnyOrder("image1.png", "image2.png");
    }

    @DisplayName("회원 정보를 갱신 한다")
    @Test
    void UpdateUserInfo() throws NoSuchElementException {
        //given
        final User user = userRepository.findByEmail(EMAIL).orElseThrow();

        final String updateNickName = "updateUser";
        final String updatePassWord = "updatePassword";
        final UserUpdateRequest request = UserUpdateRequest.builder()
                .password(updatePassWord)
                .nickname(updateNickName)
                .build();

        //when
        userService.updateUser(request, user.getId());
        final User updateUser = userRepository.findByEmail(EMAIL).orElseThrow();

        //then
        assertThat(updateUser.getNickname()).isEqualTo(updateNickName);
    }
}