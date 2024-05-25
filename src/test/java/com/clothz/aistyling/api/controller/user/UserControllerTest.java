package com.clothz.aistyling.api.controller.user;

import com.clothz.aistyling.api.controller.user.request.UserCreateRequest;
import com.clothz.aistyling.api.service.user.UserService;
import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.UserRepository;
import com.clothz.aistyling.domain.user.constant.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@MockBean(JpaMetamodelMappingContext.class)
@SpringBootTest
class UserControllerTest {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String EMAIL = "user12@gmail.com";
    private static final String ANOTHER_EMAIL = "user34@gmail.com";
    private static final String PASSWORD = "password1!";
    private static final String NICKNAME = "nickname";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    private UserController userController;

    @BeforeEach
    void beforeEach() {
            final User user = User.builder()
                    .nickname(NICKNAME)
                    .email(EMAIL)
                    .password(delegatingPasswordEncoder.encode(PASSWORD))
                    .userImages("[\"image1.png\", \"image2.png\"]")
                    .userRole(UserRole.USER)
                    .build();
            userRepository.save(user);
            clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }
        
    @DisplayName("회원 가입에 성공한다.")
    @Test
    void signUp() throws Exception {
        //given
        final UserCreateRequest userRequest = createUser(ANOTHER_EMAIL, NICKNAME, PASSWORD);
        final MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(userRequest).getBytes(StandardCharsets.UTF_8));

        //when
        //then
        mockMvc.perform(
                        multipart(HttpMethod.POST, "/api/signup")
                                .file(request)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("회원 가입할 때 이메일은 이메일 형식으로 작성해야 한다.")
    @Test
    void signUpWithEmailFormat() throws Exception{
        //given
        final UserCreateRequest userRequest = createUser("user12@gmail", NICKNAME, PASSWORD);
        final MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(userRequest).getBytes(StandardCharsets.UTF_8));

        //when
        //then
        mockMvc.perform(
                        multipart(HttpMethod.POST, "/api/signup")
                                .file(request)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일 형식으로 작성해주세요"))
                .andExpect(jsonPath("$.data").isEmpty());
        
    }
    @DisplayName("회원 가입할 때 닉네임은 3글자 이상 20글자 이하이어야 한다.")
    @Test
    void signUpWithNickname3To20Characters() throws Exception{
        //given
        final UserCreateRequest userRequest1 = createUser(EMAIL, "1", PASSWORD);
        final UserCreateRequest userRequest2 = createUser(EMAIL, "123456789012345678901", PASSWORD);
        final MockMultipartFile request1 = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(userRequest1).getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile request2 = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(userRequest2).getBytes(StandardCharsets.UTF_8));

        //when
        //then
        mockMvc.perform(
                        multipart(HttpMethod.POST, "/api/signup")
                                .file(request1)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("3에서 20자 이내여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
        // 20글자 이상의 경우
        mockMvc.perform(
                        multipart(HttpMethod.POST, "/api/signup")
                                .file(request2)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("3에서 20자 이내여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }
    @DisplayName("회원 가입할 때 비밀번호는 영문, 숫자, 특수문자가 포함되어야 하고 공백이 포함될 수 없다.")
    @Test
    void signUpWithSecurePassword() throws Exception{
        //given
        final UserCreateRequest userRequest = createUser(EMAIL, NICKNAME, "password");
        final MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(userRequest).getBytes(StandardCharsets.UTF_8));

        //when
        //then
        mockMvc.perform(
                        multipart(HttpMethod.POST, "/api/signup")
                                .file(request)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }
    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws Exception{
        //given
        final Map<String, String> usernamePasswordMap = getUsernamePasswordMap(EMAIL, PASSWORD);

        //when
        //then
        final ResultActions result = mockMvc.perform(
                post("/api/login")
                        .content(objectMapper.writeValueAsString(usernamePasswordMap))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));


    }
    @DisplayName("로그인 할 때 잘못된 비밀번호는 로그인에 실패한다.")
    @Test
    void loginWithWrongPassword() throws Exception{
        //given
        final Map<String, String> usernamePasswordMap = getUsernamePasswordMap(EMAIL, "wrongPassword");

        //when
        //then
        final ResultActions result = mockMvc.perform(
                        post("/api/login")
                                .content(objectMapper.writeValueAsString(usernamePasswordMap))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Bad credentials"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private UserCreateRequest createUser(final String email, final String nickname, final String password) {
        return UserCreateRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }

    private Map<String, String> getUsernamePasswordMap(final String email, final String password) {
        final Map<String, String> usernamePasswordMap = new LinkedHashMap<>();
        usernamePasswordMap.put(KEY_EMAIL, email);
        usernamePasswordMap.put(KEY_PASSWORD, password);
        return usernamePasswordMap;
    }
}