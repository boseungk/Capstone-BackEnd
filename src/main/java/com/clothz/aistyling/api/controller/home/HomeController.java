package com.clothz.aistyling.api.controller.home;

import com.clothz.aistyling.api.ApiResponse;
import com.clothz.aistyling.api.service.home.HomeService;
import com.clothz.aistyling.api.service.home.response.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/home")
    public ApiResponse<List<HomeResponse>> getHomeInfo() {
        List<HomeResponse> imageAndSentence = homeService.getImageAndSentence();
        return ApiResponse.ok(imageAndSentence);
    }
}
