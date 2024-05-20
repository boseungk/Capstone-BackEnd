package com.clothz.aistyling.api.service.home;

import com.clothz.aistyling.api.service.home.response.HomeResponse;
import com.clothz.aistyling.domain.home.Home;
import com.clothz.aistyling.domain.home.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class HomeService {

    private final HomeRepository homeRepository;

    public List<HomeResponse> getImageAndSentence() {
        
        List<Home> examples = homeRepository.findAll();
        return examples.stream()
                .map(HomeResponse::from)
                .collect(Collectors.toList());
    }
}
