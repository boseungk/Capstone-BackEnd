package com.clothz.aistyling.api.service.styling;

import com.clothz.aistyling.api.controller.styling.request.PromptWithWordsRequest;
import com.clothz.aistyling.api.controller.styling.request.StylingWordsRequest;
import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.api.service.styling.response.StylingImageResponse;
import com.clothz.aistyling.domain.styling.Styling;
import com.clothz.aistyling.domain.styling.StylingRepository;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StylingService {
    private final UserRepository userRepository;
    private final StylingRepository stylingRepository;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    public List<StylingExampleResponse> getImageAndPrompt() {
        // 임시 Mock Example 객체 저장, 이후 삭제 예정
        final var mockExamples = createMockExample();
        stylingRepository.saveAll(mockExamples);

        final List<Styling> examples = stylingRepository.findAll();
        return examples.stream()
                .map(StylingExampleResponse::from)
                .collect(Collectors.toList());
    }

    private List<Styling> createMockExample() {
        final Styling example1 = Styling.builder()
                .image("images1")
                .prompt("prompt example 1")
                .build();
        final Styling example2 = Styling.builder()
                .image("images2")
                .prompt("prompt example 2")
                .build();
        return List.of(example1, example2);
    }

    public StylingImageResponse getImageWithWords(final String requestUrl, final StylingWordsRequest request, final Long id) throws JsonProcessingException {
        final User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        final List<String> imageUrls = deserializeImageUrls(user.getUserImages());
        final var jsonResponse = post(requestUrl, PromptWithWordsRequest.of(request.words(), imageUrls), StylingImageResponse.class);
        return jsonResponse.block();
    }

    private List<String> deserializeImageUrls(final String imgUrls) throws JsonProcessingException {
        if (null == imgUrls)
            return List.of();
        return objectMapper.readValue(imgUrls, new TypeReference<List<String>>() {
        });
    }

    private <T> Mono<T> post(final String url, final Object request, final Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(responseType);
    }

}
