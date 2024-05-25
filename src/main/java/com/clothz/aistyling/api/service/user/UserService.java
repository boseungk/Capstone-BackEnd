package com.clothz.aistyling.api.service.user;

import com.clothz.aistyling.api.controller.user.request.UserCreateRequest;
import com.clothz.aistyling.api.controller.user.request.UserUpdateRequest;
import com.clothz.aistyling.api.service.user.response.UserImagesResponse;
import com.clothz.aistyling.api.service.user.response.UserInfoResponse;
import com.clothz.aistyling.api.service.user.response.UserSignUpResponse;
import com.clothz.aistyling.api.service.user.response.UserUpdateResponse;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.domain.user.constant.UserRole;
import com.clothz.aistyling.global.error.ErrorCode;
import com.clothz.aistyling.global.error.Exception400;
import com.clothz.aistyling.global.error.Exception404;
import com.clothz.aistyling.global.error.Exception409;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserSignUpResponse signUp(final UserCreateRequest request, final List<String> imgUrls) throws IOException {
        checkSameEmail(request.email());
        final String encodePassword = passwordEncoder.encode(request.password());
        final User user = userRepository.save(createUserEntity(request, encodePassword));
        if(!imgUrls.isEmpty()){
            uploadUserImg(imgUrls, user.getId());
        }
        return UserSignUpResponse.from(user);
    }

    private void checkSameEmail(final String email) {
        userRepository.findByEmail(email).ifPresent(n -> {
            throw new Exception409(ErrorCode.DUPLICATED_USER);
        });
    }

    private User createUserEntity(final UserCreateRequest request, final String encodePassword){
        return User.builder()
                .email(request.email())
                .nickname(request.nickname())
                .password(encodePassword)
                .userRole(UserRole.USER)
                .build();
    }

    public UserImagesResponse uploadUserImg(final List<String> imgUrls, final Long id) throws IOException {
        if(imgUrls.isEmpty())
            throw new Exception404(ErrorCode.IMAGE_NOT_FOUND);
        final User user = userRepository.findById(id).orElseThrow(
                () -> new Exception400(ErrorCode.USER_NOT_FOUND)
        );
        final String serializedImages = serializeImages(imgUrls);
        user.saveImages(serializedImages);
        userRepository.save(user);
        return UserImagesResponse.from(imgUrls);
    }

    private String serializeImages(final List<String> imgUrls) throws JsonProcessingException {
        return objectMapper.writeValueAsString(imgUrls);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(final Long id) throws JsonProcessingException {
        final User user = userRepository.findById(id).orElseThrow(
                () -> new Exception404(ErrorCode.USER_NOT_FOUND)
        );
        final var deserializedImages = deserializeImageUrls(user.getUserImages());
        return UserInfoResponse.of(user, deserializedImages);
    }

    private List<String> deserializeImageUrls(final String imgUrls) throws JsonProcessingException {
        if(null == imgUrls)
            return List.of();
        return objectMapper.readValue(imgUrls, new TypeReference<List<String>>() {});
    }

    public UserUpdateResponse updateUser(final UserUpdateRequest request, final Long id) throws NoSuchElementException {
        final User user = userRepository.findById(id).orElseThrow(() -> {
            throw new Exception400(ErrorCode.USER_NOT_FOUND);
        });
        user.updateNickname(request.nickname());
        user.updatePassword(passwordEncoder.encode(request.password()));
        final User updateUser = userRepository.save(user);
        return UserUpdateResponse.from(user);
    }

}
