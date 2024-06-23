package com.clothz.aistyling.api.controller.styling;

import com.clothz.aistyling.api.controller.styling.request.StylingWordsRequest;
import com.clothz.aistyling.api.service.styling.StylingService;
import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.domain.styling.Styling;
import com.clothz.aistyling.util.WithMockCustomUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@SpringBootTest
class StylingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private StylingService stylingService;

    @DisplayName("예시 이미지와 프롬프트를 가져온다.")
    @WithMockUser
    @Test
    void getUserInfo() throws Exception {
        //given
        given(stylingService.getImageAndPrompt()).willReturn(
                List.of(
                        StylingExampleResponse.from(new Styling("images1", "prompt example 1")),
                        StylingExampleResponse.from(new Styling("images2", "prompt example 2"))
                ));

        //when
        //then
        mockMvc.perform(get("/api/styling")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].image").value("images1"))
                .andExpect(jsonPath("$.data[0].prompt").value("prompt example 1"))
                .andExpect(jsonPath("$.data[1].image").value("images2"))
                .andExpect(jsonPath("$.data[1].prompt").value("prompt example 2"));

    }

    @DisplayName("프롬프트를 입력하면 이미지를 생성한다.")
    @WithMockCustomUser
    @Test
    void getImageWithWords() throws Exception {
        //given
        final StylingWordsRequest request = new StylingWordsRequest("스트릿");
        final var response = CompletableFuture.completedFuture("generatedImage.png");

        given(stylingService.getImageWithWords(request, 1L))
                .willReturn(response);

        //when
        //then
        mockMvc.perform(
                        post("/api/styling/words")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}