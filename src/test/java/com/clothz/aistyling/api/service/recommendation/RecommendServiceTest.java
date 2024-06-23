package com.clothz.aistyling.api.service.recommendation;

import com.clothz.aistyling.api.controller.recommendation.request.RecommendationRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
class RecommendServiceTest {

    @Autowired
    private RecommendService recommendService;
    
    @DisplayName("이미지 URL을 전달하면 GoogleLens API로 응답")
    @Test
    void getRecommendations(){
        //given
        final RecommendService mockRecommendService = mock(RecommendService.class);
        final var request = new RecommendationRequest("generatedImage.png");
        given(mockRecommendService.getRecommendations(request))
                .willReturn(Mono.just("API Response"));
        //when
        //then
        assertThat(mockRecommendService.getRecommendations(request).block()).isEqualTo("API Response");

    }
}