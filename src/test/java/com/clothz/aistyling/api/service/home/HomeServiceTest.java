package com.clothz.aistyling.api.service.home;

import com.clothz.aistyling.api.service.home.response.HomeResponse;
import com.clothz.aistyling.domain.home.Home;
import com.clothz.aistyling.domain.home.HomeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("test")
@SpringBootTest
@Transactional
public class HomeServiceTest {

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private HomeService homeService;

    @BeforeEach
    void setUp(){
        homeRepository.save(new Home("image1", "introduce1"));
        homeRepository.save(new Home("image2", "introduce2"));
    }


    @DisplayName("소개 이미지와 글 가져오기")
    @Test
    void getImageAndSentence() {
        //given
        //when
        final List<HomeResponse> imageAndSentence = homeService.getImageAndSentence();

        //then
        Assertions.assertThat(imageAndSentence)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        HomeResponse.from(new Home("image1", "introduce1")),
                        HomeResponse.from(new Home("image2", "introduce2"))
                );
    }
}
