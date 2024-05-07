package com.clothz.aistyling.domain.user;

import com.clothz.aistyling.domain.user.constant.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    private static final String EMAIL = "user12@gmail.com";
    private static final String PASSWORD = "password1!";
    private static final String NICKNAME = "nickname";

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일로 유저를 조회한다.")
    @Test
    void findByEmail(){
        //given
        User user = createUser(EMAIL, NICKNAME, PASSWORD);
        userRepository.save(user);

        //when
        User findUser = userRepository.findByEmail(EMAIL).orElse(null);

        //then
        assertThat(findUser.getEmail()).isEqualTo(EMAIL);
        assertThat(findUser.getNickname()).isEqualTo(NICKNAME);
        assertThat(findUser.getPassword()).isEqualTo(PASSWORD);
    }
    private User createUser(String email, String nickname, String password) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .userRole(UserRole.USER)
                .build();
    }
}