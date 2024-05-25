package com.clothz.aistyling.api.controller.recommendation;

import com.clothz.aistyling.api.controller.recommendation.request.RecommendationRequest;
import com.clothz.aistyling.api.service.recommendation.RecommendService;
import com.clothz.aistyling.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/recommendations")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "이미지 URL을 전달하면 GoogleLens API로 응답", description = "사용자가 이미지 URL을 전달하면 GoogleLens API로 응답 합니다.")
    @Parameter(name = "request.imgUrl", description = "이미지 URL")
    public Mono<ApiResponse<Object>> getRecommendations(@RequestBody final RecommendationRequest request){
        final var response = recommendService.getRecommendations(request);
        return response.flatMap(data -> Mono.just(ApiResponse.ok(data)));
    }
}
