package com.clothz.aistyling.api.service.recommendation;

import com.clothz.aistyling.api.controller.recommendation.request.RecommendationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {
    private static final String ENGINE = "engine";
    private static final String GOOGLE_LENS = "google_lens";
    private static final String URL = "url";
    private static final String HL = "hl";
    private static final String KO = "ko";
    private static final String COUNTRY = "country";
    private static final String KR = "kr";
    private static final String API_KEY = "api_key";
    
    @Value("${spring.cloud.google.search.serp.api.key}")
    private String googleSearchSerpApiKey;
    private final WebClient webClient;

    public Mono<Object> getRecommendations(final RecommendationRequest request){
        return getSerpApiResponse(request);
    }

    private Mono<Object> getSerpApiResponse(RecommendationRequest request) {
        return webClient.get()
                .uri(baseUrl -> baseUrl
                        .queryParam(ENGINE, GOOGLE_LENS)
                        .queryParam(URL, request.imgUrl())
                        .queryParam(HL, KO)
                        .queryParam(COUNTRY, KR)
                        .queryParam(API_KEY, googleSearchSerpApiKey)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class);
    }
}
