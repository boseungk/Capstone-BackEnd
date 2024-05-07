package com.clothz.aistyling.api.service.styling;

import com.clothz.aistyling.api.controller.styling.request.StylingWordsRequest;
import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.api.service.styling.response.StylingImageResponse;
import com.clothz.aistyling.domain.styling.Styling;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.domain.user.constant.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StylingServiceTest {
    private static final String EMAIL = "user12@gmail.com";
    private static final String NICKNAME = "user";
    private static final String PASSWORD = "password1!";
    private static final String AI_URL_WITH_WORDS = "/api/styling/words";
    private static final int HTTP_OK = 200;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StylingService stylingService;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockWebServer mockWebServer;
    private static String mockWebServerUrl;

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

        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServerUrl = mockWebServer.url(AI_URL_WITH_WORDS).toString();

    }
    private void clear() {
        em.flush();
        em.clear();
    }

    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
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
    void getImageWithWords() throws JsonProcessingException {
        //given
        final StylingWordsRequest wordsRequest = StylingWordsRequest.builder()
                .words("스트릿 패션")
                .build();
        final StylingImageResponse mockResponse = StylingImageResponse.builder()
                .images(List.of("generatedImage1.png"))
                .build();
        final User user = userRepository.findByEmail(EMAIL).orElseThrow(() -> new IllegalArgumentException("User not found"));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(objectMapper.writeValueAsString(mockResponse))
                .addHeader("Content-Type", "application/json"));

        //when
        final var imageWithWords = stylingService.getImageWithWords(mockWebServerUrl, wordsRequest, user.getId());

        //then
        assertThat(imageWithWords.images())
                .hasSize(1)
                .containsExactlyInAnyOrder("generatedImage1.png");
    }
}