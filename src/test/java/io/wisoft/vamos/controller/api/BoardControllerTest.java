package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.FileInputStream;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("게시글 API 테스트")
class BoardControllerTest {

    Logger logger = LoggerFactory.getLogger(BoardControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final User user2 = getUser("testId2", "01023456789", "고등동 사람", 127.103018, 37.429073, "경기도 성남시 수정구 고등동");
    private final User user3 = getUser("testId3", "01034567890", "삼평동 사람", 127.10459896729914, 37.40269721785548, "경기도 성남시 분당구 삼평동");
    private final User user4 = getUser("testId4", "01045678901", "심곡동 사람", 127.100001, 37.441566, "경기도 성남시 분당구 심곡동");

    private User getUser(String username, String number, String nickname, double longitude, double latitude, String addressName) {
        return User.from(username, "1234", PhoneNumber.of(number), nickname
                , UserLocation.from(longitude, latitude, addressName));
    }

    @BeforeAll
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
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
                .andExpect(jsonPath("$.data.user_info.nickname", is("고등동 사람")))
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
                .andExpect(jsonPath("$.data.user_info.username", is("testId2")))
                .andExpect(jsonPath("$.data.user_info.nickname", is("고등동 사람")))
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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.message", is("올바른 파일형식이 아닙니다.")))
        ;
    }

    @Test
    @WithMockUser(username = "testId3", password = "1234")
    @DisplayName("04. 게시글들 조회 테스트 (삼평동 사람이 글조회(2KM 이내) -> 고등동 사람의 글은 보이지 않음)")
    void _04_get_boards_test() throws Exception {

        final ResultActions result = mockMvc.perform(
                get("/api/boards")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(0)))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @WithMockUser(username = "testId4", password = "1234")
    @DisplayName("05. 게시글들 조회 테스트 (심곡동 사람이 글조회(2KM 이내) -> 고등동 사람의 글이 보임)")
    void _05_get_boards_test() throws Exception {

        final ResultActions result = mockMvc.perform(
                get("/api/boards")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(2)))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("위경도 거리 계산 테스트")
    void __distanceTest() {
        final double distance1 = getDistance(37.40269721785548, 127.10459896729914, 37.429073, 127.103018);
        logger.info("calc distance(삼평동, 고등동) = {}", distance1); //2.936km

        final double distance2 = getDistance(37.441566, 127.100001, 37.429073, 127.103018);
        logger.info("calc distance(심곡동, 고등동) = {}", distance2); //1.414km
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return (dist);
        }
    }
}