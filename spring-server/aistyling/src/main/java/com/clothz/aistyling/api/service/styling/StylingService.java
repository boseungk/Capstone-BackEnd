package com.clothz.aistyling.api.service.styling;

import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.domain.styling.Styling;
import com.clothz.aistyling.domain.styling.StylingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StylingService {
    private final StylingRepository stylingRepository;
    public List<StylingExampleResponse> getImageAndPrompt() {
        // 임시 Mock Example 객체 저장, 이후 삭제 예정
        final var mockExamples = createMockExample();
        stylingRepository.saveAll(mockExamples);

        List<Styling> examples = stylingRepository.findAll();
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
}
