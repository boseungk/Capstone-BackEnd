package com.clothz.aistyling.api.controller.user;

import com.clothz.aistyling.global.api.ApiResponse;
import com.clothz.aistyling.api.controller.user.request.UserCreateRequest;
import com.clothz.aistyling.api.controller.user.request.UserUpdateRequest;
import com.clothz.aistyling.api.service.user.UserService;
import com.clothz.aistyling.api.service.user.response.UserImagesResponse;
import com.clothz.aistyling.api.service.user.response.UserInfoResponse;
import com.clothz.aistyling.api.service.user.response.UserSignUpResponse;
import com.clothz.aistyling.api.service.user.response.UserUpdateResponse;
import com.clothz.aistyling.global.jwt.userInfo.CustomUserDetails;
import com.clothz.aistyling.global.aws.S3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Slf4j
@Tag(name = "UserController", description = "회원정보 조회하기, 수정하기, 사진변경, 회원가입")
public class UserController {
    private final UserService userService;
    private final S3Service s3Service;

    @PostMapping(value="/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "사용자 이메일, 닉네임, 비밀번호를 입력받아 회원가입한다")
    @Parameters({
            @Parameter(name = "request.email", description = "이메일 형식으로 작성해주세요"),
            @Parameter(name = "request.nickname", description = "3에서 20자 이내여야 합니다"),
            @Parameter(name = "request.password", description = "8에서 20자 이내여야 합니다. " +
                    "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
    })
    public ApiResponse<UserSignUpResponse> signUp(
            @RequestPart("request") @Valid
            final UserCreateRequest request,
            @RequestPart(value="images", required = false)
            final List<MultipartFile> images
    ) throws IOException {
        final List<String> imgUrls = new ArrayList<>();
        if(null != images)
            imgUrls.addAll(s3Service.upload(images));
        final var userSingUpResponse = userService.signUp(request, imgUrls);
        return ApiResponse.ok(userSingUpResponse);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "회원정보 조회하기", description = "토큰을 기반으로 사용자의 회원정보를 조회한다")
    public ApiResponse<UserInfoResponse> getUserInfo(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) throws JsonProcessingException {
        final var userInfoResponse = userService.getUserInfo(userDetails.getId());
        return ApiResponse.ok(userInfoResponse);
    }

    @PostMapping(value = "/users/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "회원정보 사진 변경", description = "토큰을 기반으로 사용자의 이미지를 변경한다")
    @Parameter(name = "images", description = "이미지들을 업로드 합니다")
    public ApiResponse<UserImagesResponse> uploadUserImg(
            @RequestPart("images") final List<MultipartFile> images,
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) throws IOException {
        final var imgUrls = s3Service.upload(images);
        final var userImagesResponse = userService.uploadUserImg(imgUrls, userDetails.getId());
        return ApiResponse.ok(userImagesResponse);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "회원정보 수정하기", description = "토큰을 기반으로 사용자의 회원정보를 수정한다")
    @Parameters({
            @Parameter(name = "request.nickname", description = "3에서 20자 이내여야 합니다."),
            @Parameter(name = "request.password", description = "8에서 20자 이내여야 합니다. " +
                    "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
    })
    public ApiResponse<UserUpdateResponse> updateUser(@RequestBody final UserUpdateRequest request, @AuthenticationPrincipal final CustomUserDetails userDetails) throws JsonProcessingException{
        final UserUpdateResponse response = userService.updateUser(request, userDetails.getId());
        return ApiResponse.ok(response);
    }
}
