package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.FileInputStream;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("게시글 API 테스트")
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final User user = User.from("testId2", "1234", PhoneNumber.of("01023456789"), "tester2"
            , UserLocation.from(0.0, 0.0, "test location"));

    @BeforeAll
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "testId2", password = "1234")
    @DisplayName("01. 게시글 업로드 성공 테스트 (첨부파일 O)")
    void _01_upload_board_succeed_test() throws Exception {

        FileInputStream fis = new FileInputStream("/Users/jun/Downloads/penguin.png");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "files",
                "penguin.png",
                "image/png",
                fis);

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        ResultActions result = mockMvc.perform(
                multipart("/api/board")
                        .file(multipartFile)
                        .param("title", "글제목")
                        .param("content", "글내용")
                        .param("price", "1")
                        .param("categoryNameEN", "DIGITAL_DEVICE")
                        .contentType(mediaType)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("글제목")))
                .andExpect(jsonPath("$.data.content", is("글내용")))
                .andExpect(jsonPath("$.data.price", is(1)))
                .andExpect(jsonPath("$.data.user_info.username", is("testId2")))
                .andExpect(jsonPath("$.data.user_info.nickname", is("tester2")))
                .andExpect(jsonPath("$.data.user_info.phone_number.value", is("01023456789")))
                .andExpect(jsonPath("$.data.category_info.category_name[0]", is("디지털기기")))
                .andExpect(jsonPath("$.data.category_info.category_name[1]", is("DIGITAL_DEVICE")))
                .andExpect(jsonPath("$.data.status", is("SALE")))
                .andExpect(jsonPath("$.data.photos.length()", is(1)))
                .andExpect(jsonPath("$.data.photos[0].original_file_name", is("penguin.png")))
                .andExpect(jsonPath("$.data.photos[0].extension", is("png")))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @WithMockUser(username = "testId2", password = "1234")
    @DisplayName("02. 게시글 업로드 성공 테스트 (첨부파일 X)")
    void _02_upload_board_succeed_test() throws Exception {

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        ResultActions result = mockMvc.perform(
                multipart("/api/board")
                        .param("title", "글제목")
                        .param("content", "글내용")
                        .param("price", "1")
                        .param("categoryNameEN", "DIGITAL_DEVICE")
                        .contentType(mediaType)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("글제목")))
                .andExpect(jsonPath("$.data.content", is("글내용")))
                .andExpect(jsonPath("$.data.price", is(1)))
                .andExpect(jsonPath("$.data.user_info.id", is(1)))
                .andExpect(jsonPath("$.data.user_info.username", is("testId2")))
                .andExpect(jsonPath("$.data.user_info.nickname", is("tester2")))
                .andExpect(jsonPath("$.data.user_info.phone_number.value", is("01023456789")))
                .andExpect(jsonPath("$.data.category_info.category_name[0]", is("디지털기기")))
                .andExpect(jsonPath("$.data.category_info.category_name[1]", is("DIGITAL_DEVICE")))
                .andExpect(jsonPath("$.data.status", is("SALE")))
                .andExpect(jsonPath("$.data.photos.length()", is(0)))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @WithMockUser(username = "testId2", password = "1234")
    @DisplayName("03. 게시글 업로드 실패 테스트 (유효하지 않은 파일 확장자)")
    void _03_upload_board_failed_test() throws Exception {

        FileInputStream fis = new FileInputStream("/Users/jun/Downloads/example.pdf");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "files",
                "penguin.png",
                "image/png",
                fis);

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        ResultActions result = mockMvc.perform(
                multipart("/api/board")
                        .file(multipartFile)
                        .param("title", "글제목")
                        .param("content", "글내용")
                        .param("price", "1")
                        .param("categoryNameEN", "DIGITAL_DEVICE")
                        .contentType(mediaType)
        );

        result.andDo(print())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error_message", is("올바른 파일형식이 아닙니다.")));
    }
}