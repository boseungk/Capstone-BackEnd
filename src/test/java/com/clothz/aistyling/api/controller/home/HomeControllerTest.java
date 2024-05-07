package com.clothz.aistyling.api.controller.home;

import com.clothz.aistyling.api.service.home.HomeService;
import com.clothz.aistyling.api.service.home.response.HomeResponse;
import com.clothz.aistyling.domain.home.Home;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class)
public class HomeControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean
    HomeService homeService;

    @WithMockUser
    @DisplayName("소개 이미지와 글을 가져온다")
    @Test
    void getHomeInfo() throws Exception {
        //given
        given(homeService.getImageAndSentence()).willReturn(
                List.of(
                        HomeResponse.from(new Home("image1", "introduce1")),
                        HomeResponse.from(new Home("image2", "introduce2"))
                ));

        //when
        //then
        mockMvc.perform(get("/api/home")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].image").value("image1"))
                .andExpect(jsonPath("$.data[0].sentence").value("introduce1"))
                .andExpect(jsonPath("$.data[1].image").value("image2"))
                .andExpect(jsonPath("$.data[1].sentence").value("introduce2"));

    }
}
