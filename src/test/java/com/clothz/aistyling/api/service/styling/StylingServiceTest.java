package com.clothz.aistyling.api.service.styling;

import com.clothz.aistyling.api.controller.styling.request.StylingWordsRequest;
import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.domain.styling.Styling;
import com.clothz.aistyling.domain.styling.StylingRepository;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.domain.user.constant.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@SpringBootTest
@Transactional
class StylingServiceTest {
    private static final String EMAIL = "user12@gmail.com";
    private static final String NICKNAME = "user";
    private static final String PASSWORD = "password1!";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StylingService stylingService;

    @Autowired
    private EntityManager em;

    @Autowired
    private StylingRepository stylingRepository;

    @BeforeEach
    void setUp() throws IOException {
        final User user = User.builder()
                .nickname(NICKNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .userRole(UserRole.USER)
                .userImages("[\"image1.png\", \"image2.png\"]")
                .build();
        userRepository.save(user);
        stylingRepository.save(new Styling("images1", "prompt example 1"));
        stylingRepository.save(new Styling("images2", "prompt example 2"));

    }
    private void clear() {
        em.flush();
        em.clear();
    }


    @DisplayName("예시 이미지와 프롬프트를 가져온다.")
    @Test
    void getImageAndPrompt(){
        //given
        //when
        final var imageAndPrompt = stylingService.getImageAndPrompt();

        //then
        assertThat(imageAndPrompt)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        StylingExampleResponse.from(new Styling("images1", "prompt example 1")),
                        StylingExampleResponse.from(new Styling("images2", "prompt example 2"))
                );
    }

    @DisplayName("스타일 관련 단어를 통해 AI API 서버로부터 이미지를 응답 받는다.")
    @Test
    void getImageWithWords() throws Exception {
        //given
        final StylingWordsRequest wordsRequest = StylingWordsRequest.builder()
                .inputs("스트릿 패션")
                .build();
        final User user = userRepository.findByEmail(EMAIL).orElseThrow(() -> new IllegalArgumentException("User not found"));

        //when
        final var imageWithWords = stylingService.getImageWithWords(wordsRequest, user.getId());
        imageWithWords.complete("generatedImage1.png");

        //then
        assertThat(imageWithWords.get()).isEqualTo("generatedImage1.png");
    }
}