package com.clothz.aistyling.api.controller.styling;

import com.clothz.aistyling.global.api.ApiResponse;
import com.clothz.aistyling.api.controller.styling.request.StylingWordsRequest;
import com.clothz.aistyling.api.service.styling.StylingService;
import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.global.jwt.userInfo.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Tag(name = "StylingController", description = "스타일 예시 및 문장 예시, 스타일 이미지 선택")
public class StylingController {
    private final StylingService stylingService;

    @GetMapping("/styling")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "스타일 예시 및 문장 예시", description = "좋은 생성형 이미지, 좋은 프롬프트 예시")
    public ApiResponse<List<StylingExampleResponse>> getStylingExample(
            @AuthenticationPrincipal final CustomUserDetails userDetails){
        final var imageAndPrompt = stylingService.getImageAndPrompt();
        return ApiResponse.ok(imageAndPrompt);
    }

    @PostMapping("/styling/words")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "스타일 이미지 선택", description = "사용자가 이미지를 클릭하면 관련 단어를 서버로 전달")
    @Parameter(name = "request.inputs", description = "이미지 관련 단어들")
    public CompletableFuture<ApiResponse<String>> getImageWithWords(
            @RequestBody @Valid final StylingWordsRequest request,
            @AuthenticationPrincipal final CustomUserDetails userDetails) throws JsonProcessingException {
        final var imageResponseFuture = stylingService.getImageWithWords(request, userDetails.getId());
        return imageResponseFuture.thenApply(ApiResponse::ok);
    }

    @PostMapping("/styling/sentences")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "스타일 이미지 선택", description = "사용자가 이미지를 클릭하면 관련 문장을 서버로 전달")
    @Parameter(name = "request.inputs", description = "이미지 관련 단어들")
    public CompletableFuture<ApiResponse<String>> getImageWithSentences(
            @RequestBody @Valid final StylingWordsRequest request,
            @AuthenticationPrincipal final CustomUserDetails userDetails) throws JsonProcessingException {
        final var imageResponseFuture = stylingService.getImageWithSentences(request, userDetails.getId());
        return imageResponseFuture.thenApply(ApiResponse::ok);
    }
}
