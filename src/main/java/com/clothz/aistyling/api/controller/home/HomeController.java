package com.clothz.aistyling.api.controller.home;

import com.clothz.aistyling.api.ApiResponse;
import com.clothz.aistyling.api.service.home.HomeService;
import com.clothz.aistyling.api.service.home.response.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Tag(name = "HomeController", description = "소개 및 메인 화면")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/home")
    @Operation(summary = "소개 및 메인 화면", description = "서비스를 표현할 수 있는 이미지 및 글 작성")
    public ApiResponse<List<HomeResponse>> getHomeInfo() {
        List<HomeResponse> imageAndSentence = homeService.getImageAndSentence();
        return ApiResponse.ok(imageAndSentence);
    }
}
