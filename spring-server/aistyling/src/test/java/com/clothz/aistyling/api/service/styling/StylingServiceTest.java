package com.clothz.aistyling.api.service.styling;

import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.domain.styling.Styling;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StylingServiceTest {
    @Autowired
    private StylingService stylingService;

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
}