package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.category.CategoryName;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.Role;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.exception.RestControllersExceptionHandler;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CategoryRepository;
import io.wisoft.vamos.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("pgtest")
@DisplayName("게시글 API 통합 테스트")
class BoardControllerTest {

//    private MockMvc mockMvc;
//
//    private final MockHttpSession session = new MockHttpSession();
//
//    @Autowired
//    private BoardController boardController;
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @AfterEach
//    void clear() {
//        boardRepository.deleteAll();
//        userRepository.deleteAll();
//        session.clearAttributes();
//    }
//
//    @BeforeEach
//    void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
//                .setCustomArgumentResolvers(new LoginUserArgumentResolver(session))
//                .setControllerAdvice(new RestControllersExceptionHandler())
//                .build();
//    }
//
//    @Test
//    @DisplayName("게시글 업로드 성공 테스트 [첨부파일 O]")
//    void boardUpload_succeed_test() throws Exception {
//        //given
//        FileInputStream fis = new FileInputStream("/Users/jun/Downloads/penguin.png");
//        MockMultipartFile multipartFile = new MockMultipartFile(
//                "files",
//                "penguin.png",
//                "image/png",
//                fis);
//
//        HashMap<String, String> contentTypeParams = new HashMap<>();
//        contentTypeParams.put("boundary", "265001916915724");
//        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
//
//        String username = "testId2";
//        String email = "testId2@gmail.com";
//        User user = User.builder()
//                .username(username)
//                .email(email)
//                .phoneNumber(PhoneNumber.of("01023456789"))
//                .role(Role.USER)
//                .location(UserLocation.from(127.103018, 37.429073, "경기도 성남시 수정구 고등동"))
//                .build();
//        userRepository.save(user);
//        session.setAttribute("user", new SessionUser(user));
//
//        //when
//        ResultActions result = mockMvc.perform(
//                multipart("/api/board")
//                        .file(multipartFile)
//                        .param("title", "글제목")
//                        .param("content", "글내용")
//                        .param("price", "1")
//                        .param("categoryNameEN", "DIGITAL_DEVICE")
//                        .contentType(mediaType)
//        );
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.title", is("글제목")))
//                .andExpect(jsonPath("$.data.content", is("글내용")))
//                .andExpect(jsonPath("$.data.price", is(1)))
//                .andExpect(jsonPath("$.data.user_info.username", is("testId2")))
//                .andExpect(jsonPath("$.data.user_info.email", is("testId2@gmail.com")))
//                .andExpect(jsonPath("$.data.user_info.phone_number.value", is("01023456789")))
//                .andExpect(jsonPath("$.data.category_info.category_name[0]", is("디지털기기")))
//                .andExpect(jsonPath("$.data.category_info.category_name[1]", is("DIGITAL_DEVICE")))
//                .andExpect(jsonPath("$.data.status", is("SALE")))
//                .andExpect(jsonPath("$.data.photos.length()", is(1)))
//                .andExpect(jsonPath("$.data.photos[0].original_file_name", is("penguin.png")))
//                .andExpect(jsonPath("$.data.photos[0].extension", is("png")))
//                .andExpect(jsonPath("$.error_message").doesNotExist())
//        ;
//    }
//
//    @Test
//    @DisplayName("게시글 업로드 성공 테스트 [첨부파일 X]")
//    void uploadBoard_succeed_test2() throws Exception {
//        //given
//        HashMap<String, String> contentTypeParams = new HashMap<>();
//        contentTypeParams.put("boundary", "265001916915724");
//        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
//
//        String username = "testId2";
//        String email = "testId2@gmail.com";
//        User user = User.builder()
//                .username(username)
//                .email(email)
//                .phoneNumber(PhoneNumber.of("01023456789"))
//                .role(Role.USER)
//                .location(UserLocation.from(127.103018, 37.429073, "경기도 성남시 수정구 고등동"))
//                .build();
//        userRepository.save(user);
//        session.setAttribute("user", new SessionUser(user));
//
//        //when
//        ResultActions result = mockMvc.perform(
//                multipart("/api/board")
//                        .param("title", "글제목")
//                        .param("content", "글내용")
//                        .param("price", "1")
//                        .param("categoryNameEN", "DIGITAL_DEVICE")
//                        .contentType(mediaType)
//        );
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.title", is("글제목")))
//                .andExpect(jsonPath("$.data.content", is("글내용")))
//                .andExpect(jsonPath("$.data.price", is(1)))
//                .andExpect(jsonPath("$.data.user_info.username", is("testId2")))
//                .andExpect(jsonPath("$.data.user_info.email", is("testId2@gmail.com")))
//                .andExpect(jsonPath("$.data.user_info.phone_number.value", is("01023456789")))
//                .andExpect(jsonPath("$.data.category_info.category_name[0]", is("디지털기기")))
//                .andExpect(jsonPath("$.data.category_info.category_name[1]", is("DIGITAL_DEVICE")))
//                .andExpect(jsonPath("$.data.status", is("SALE")))
//                .andExpect(jsonPath("$.data.photos.length()", is(0)))
//                .andExpect(jsonPath("$.error_message").doesNotExist())
//        ;
//    }
//
//    @Test
//    @DisplayName("게시글 업로드 실패 테스트 (유효하지 않은 파일 확장자)")
//    void _03_upload_board_failed_test() throws Exception {
//
//        FileInputStream fis = new FileInputStream("/Users/jun/Downloads/example.pdf");
//        MockMultipartFile multipartFile = new MockMultipartFile(
//                "files",
//                "penguin.png",
//                "image/png",
//                fis);
//
//        String username = "testId2";
//        String email = "testId2@gmail.com";
//        User user = User.builder()
//                .username(username)
//                .email(email)
//                .phoneNumber(PhoneNumber.of("01023456789"))
//                .role(Role.USER)
//                .location(UserLocation.from(127.103018, 37.429073, "경기도 성남시 수정구 고등동"))
//                .build();
//        userRepository.save(user);
//        session.setAttribute("user", new SessionUser(user));
//
//        HashMap<String, String> contentTypeParams = new HashMap<>();
//        contentTypeParams.put("boundary", "265001916915724");
//        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
//
//        ResultActions result = mockMvc.perform(
//                multipart("/api/board")
//                        .file(multipartFile)
//                        .param("title", "글제목")
//                        .param("content", "글내용")
//                        .param("price", "1")
//                        .param("categoryNameEN", "DIGITAL_DEVICE")
//                        .contentType(mediaType)
//        );
//
//        result.andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("$.data").doesNotExist())
//                .andExpect(jsonPath("$.error.message", is("올바른 파일형식이 아닙니다.")))
//        ;
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("게시글들 조회 테스트 (심곡동 사람이 글조회(2KM 이내) -> 고등동 사람의 글이 보임)")
//    void get_boards_test1() throws Exception {
//        //given
//        /* 고등동 사람 글 등록 */
//        User user = User.builder()
//                .username("testId2")
//                .email("testId2@gmail.com")
//                .phoneNumber(PhoneNumber.of("01023456789"))
//                .role(Role.USER)
//                .location(UserLocation.from(127.103018, 37.429073, "경기도 성남시 수정구 고등동"))
//                .build();
//        userRepository.save(user);
//        Category category = categoryRepository.findByName(CategoryName.DIGITAL_DEVICE).get();
//        Board board = Board.builder()
//                .title("title")
//                .content("content")
//                .price(1000)
//                .category(category)
//                .user(user)
//                .build();
//        boardRepository.save(board);
//        /*                 */
//
//        /* 심곡동 사람 생성 */
//        User user2 = User.builder()
//                .username("testId3")
//                .email("testId3@gmail.com")
//                .phoneNumber(PhoneNumber.of("01012345678"))
//                .role(Role.USER)
//                .location(UserLocation.from(127.100001, 37.441566, "경기도 성남시 분당구 심곡동"))
//                .build();
//        userRepository.save(user2);
//        session.setAttribute("user", new SessionUser(user2));
//        /*              */
//
//        //when
//        ResultActions result = mockMvc.perform(
//                get("/api/boards")
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()", is(1)))
//                .andExpect(jsonPath("$.error_message").doesNotExist())
//        ;
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("게시글들 조회 테스트 (삼평동 사람이 글조회(2KM 이내) -> 고등동 사람의 글이 보이지 않음)")
//    void get_boards_test2() throws Exception {
//        //given
//        /* 고등동 사람 글 등록 */
//        User user = User.builder()
//                .username("testId2")
//                .email("testId2@gmail.com")
//                .phoneNumber(PhoneNumber.of("01023456789"))
//                .role(Role.USER)
//                .location(UserLocation.from(127.103018, 37.429073, "경기도 성남시 수정구 고등동"))
//                .build();
//        userRepository.save(user);
//        Category category = categoryRepository.findByName(CategoryName.DIGITAL_DEVICE).get();
//        Board board = Board.builder()
//                .title("title")
//                .content("content")
//                .price(1000)
//                .category(category)
//                .user(user)
//                .build();
//        boardRepository.save(board);
//        /*                 */
//
//        /* 삼평동 사람 생성 */
//        User user2 = User.builder()
//                .username("testId3")
//                .email("testId3@gmail.com")
//                .phoneNumber(PhoneNumber.of("01012345678"))
//                .role(Role.USER)
//                .location(UserLocation.from(127.10459896729914, 37.40269721785548, "경기도 성남시 분당구 삼평동"))
//                .build();
//        userRepository.save(user2);
//        session.setAttribute("user", new SessionUser(user2));
//        /*              */
//
//        //when
//        ResultActions result = mockMvc.perform(
//                get("/api/boards")
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()", is(0)))
//                .andExpect(jsonPath("$.error_message").doesNotExist())
//        ;
//    }
}
