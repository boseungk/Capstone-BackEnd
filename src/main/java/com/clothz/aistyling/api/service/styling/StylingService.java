package com.clothz.aistyling.api.service.styling;

import com.clothz.aistyling.api.controller.styling.request.PromptWithWordsRequest;
import com.clothz.aistyling.api.controller.styling.request.StylingWordsRequest;
import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.domain.styling.Styling;
import com.clothz.aistyling.domain.styling.StylingRepository;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.global.error.ErrorCode;
import com.clothz.aistyling.global.error.Exception400;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StylingService {
    @Value("${spring.cloud.aws.sqs.queue.url1}")
    private String requestWordsQueueUrl;
    private final SqsTemplate sqsTemplate;
    private final UserRepository userRepository;
    private final StylingRepository stylingRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConcurrentLinkedQueue<CompletableFuture<String>> queue = new ConcurrentLinkedQueue<>();

    public List<StylingExampleResponse> getImageAndPrompt() {
        final List<Styling> examples = stylingRepository.findAll();
        return examples.stream()
                .map(StylingExampleResponse::from)
                .collect(Collectors.toList());
    }

    public CompletableFuture<String> getImageWithWords(final StylingWordsRequest request, final Long id) throws JsonProcessingException {
        final User user = userRepository.findById(id).orElseThrow(
                () -> new Exception400(ErrorCode.USER_NOT_FOUND)
        );
        final List<String> imageUrls = deserializeImageUrls(user.getUserImages());
        sendRequestAIServer(request.inputs(), imageUrls);
        initializeResponseOrderManagement();
        return waitForAIResponse();
    }

    private List<String> deserializeImageUrls(final String imgUrls) throws JsonProcessingException {
        if (null == imgUrls) return List.of();
        return objectMapper.readValue(imgUrls, new TypeReference<List<String>>() {
        });
    }

    private CompletableFuture<String> waitForAIResponse() {
        return Objects.requireNonNull(queue.peek())
                .thenCompose(s -> queue.poll());
    }

    private void initializeResponseOrderManagement() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        queue.add(future);
    }

    private void sendRequestAIServer(String inputs, List<String> imageUrls) {
        sqsTemplate.send(requestWordsQueueUrl, PromptWithWordsRequest.of(inputs, imageUrls));
    }

    @SqsListener("responseQueue")
    private void receiveMessage(final String message) throws JsonProcessingException {
        if(message == null) return;
        final String parseMessage = parseMessage(message);
        completeAIResponse(parseMessage);
    }

    private String parseMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, String.class);
    }

    private void completeAIResponse(String responseMessage) {
        final CompletableFuture<String> pendingFuture = queue.peek();
        if (null != pendingFuture)
            pendingFuture.complete(responseMessage);
    }
}
