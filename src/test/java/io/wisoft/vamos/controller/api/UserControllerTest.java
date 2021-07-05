package io.wisoft.vamos.controller.api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.config.BeanIds;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletException;
import java.util.HashMap;

import static io.wisoft.vamos.util.JsonUtils.toJson;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("사용자 API 테스트")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    static String token;

    @BeforeAll
    void init() throws ServletException {

        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        delegatingFilterProxy.init(
                new MockFilterConfig(webApplicationContext.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN))
        ;

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .addFilters(delegatingFilterProxy)
                .build();
    }

    @Test
    @DisplayName("01. 사용자 회원가입 성공 테스트")
    void _01_user_join_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<>() {
                                            {
                                                put("username", "testId");
                                                put("password", "1234");
                                                put("nickname", "tester");
                                                put("phoneNumber", "01012345678");
                                                put("x", 127.10459896729914);
                                                put("y", 37.40269721785548);
                                                put("addressName", "경기도 성남시 분당구 삼평동");
                                            }
                                        }
                                )
                        )
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(6)))
                .andExpect(jsonPath("$.data.username", is("testId")))
                .andExpect(jsonPath("$.data.nickname", is("tester")))
                .andExpect(jsonPath("$.data.phone_number.value", is("01012345678")))
                .andExpect(jsonPath("$.data.user_roles[0].role_name", is("ROLE_USER")))
                .andExpect(jsonPath("$.data.location.x", is(127.10459896729914)))
                .andExpect(jsonPath("$.data.location.y", is(37.40269721785548)))
                .andExpect(jsonPath("$.data.location.addressName", is("경기도 성남시 분당구 삼평동")))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("02. 사용자 회원가입 실패 테스트 (id 중복)")
    void _02_user_join_failed_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<>() {
                                            {
                                                put("username", "testId");
                                                put("password", "1234");
                                                put("nickname", "tester2");
                                                put("phoneNumber", "01023456789");
                                                put("x", 127.10459896729914);
                                                put("y", 37.40269721785548);
                                                put("addressName", "경기도 성남시 분당구 삼평동");
                                            }
                                        }
                                ))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error_message").exists())
        ;
    }

    @Test
    @DisplayName("03. 사용자 회원가입 실패 테스트 (nickname 중복)")
    void _03_user_join_failed_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(
                                new HashMap<>() {
                                    {
                                        put("username", "testId2");
                                        put("password", "1234");
                                        put("nickname", "tester");
                                        put("phoneNumber", "01023456789");
                                        put("x", 127.10459896729914);
                                        put("y", 37.40269721785548);
                                        put("addressName", "경기도 성남시 분당구 삼평동");
                                    }
                                }
                        ))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error_message").exists())
        ;
    }

    @Test
    @DisplayName("04. 사용자 회원가입 실패 테스트 (phoneNumber 중복)")
    void _04_user_join_failed_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(
                                new HashMap<>() {
                                    {
                                        put("username", "testId2");
                                        put("password", "1234");
                                        put("nickname", "tester2");
                                        put("phoneNumber", "01012345678");
                                        put("x", 127.10459896729914);
                                        put("y", 37.40269721785548);
                                        put("addressName", "경기도 성남시 분당구 삼평동");
                                    }
                                }
                        ))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error_message").exists());
    }

    @Test
    @DisplayName("05. 사용자 로그인 테스트")
    void _05_user_login_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<>() {
                                            {
                                                put("username", "testId");
                                                put("password", "1234");
                                            }
                                        }
                                )
                        )
        );

        String response = result
                .andReturn()
                .getResponse()
                .getContentAsString();

        int tokenStartIndex = response.indexOf("token");

        String body = response.substring(tokenStartIndex);

        token = body.split("\"")[2].trim();

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token", is(token)))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("06. 특정 사용자 조회 성공 테스트")
    void _06_get_user_info_test() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/api/user?username=testId")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", is("testId")))
                .andExpect(jsonPath("$.data.nickname", is("tester")))
                .andExpect(jsonPath("$.data.phone_number.value", is("01012345678")))
                .andExpect(jsonPath("$.data.user_roles[0].role_name", is("ROLE_USER")))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("07. 특정 사용자 조회 실패 테스트 (존재하지 않는 사용자)")
    void _07_get_user_info_failed_test() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/api/user?username=InvalidUser++")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error_message", is("존재하지 않는 사용자 입니다.")))
        ;
    }

    @Test
    @DisplayName("08. 특정 사용자 조회 실패 테스트 (token 검증 실패)")
    void _08_get_user_info_failed_test() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/api/user?username=testId")
                        .header("Authorization", "Bearer " + "InValid token")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("잘못된 JWT 서명 입니다.")))
                .andExpect(jsonPath("$.code", is("JWT003")))
                .andExpect(jsonPath("$.status", is(401)))
        ;
    }

    @Test
    @DisplayName("09. 모든 사용자 조회 실패 테스트 (관리자 권한을 갖는 사용자만 조회 가능)")
    void _09_get_all_user_info_failed_test() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/api/users")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("접근 허용이 거부되었습니다.")))
                .andExpect(jsonPath("$.code", is("C001")))
                .andExpect(jsonPath("$.status", is(403)))
        ;
    }

    @Test
    @DisplayName("10. 사용자 로그아웃 테스트 (사용자 id -> testId)")
    void _10_user_logout_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.body", is("로그아웃 되었습니다.")))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("11. 사용자 로그아웃 실패 테스트 (이미 로그아웃한 사용자 -> testId)")
    void _11_user_logout_failed_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("로그아웃된 토큰입니다.")))
                .andExpect(jsonPath("$.code", is("JWT002")))
                .andExpect(jsonPath("$.status", is(401)))
        ;
    }

    @Test
    @DisplayName("12. 관리자 로그인 테스트")
    void _12_admin_login_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<>() {
                                            {
                                                put("username", "admin");
                                                put("password", "1234");
                                            }
                                        }
                                )
                        )
        );

        String response = result
                .andReturn()
                .getResponse()
                .getContentAsString();

        int tokenStartIndex = response.indexOf("token");

        String body = response.substring(tokenStartIndex);

        token = body.split("\"")[2].trim();

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token", is(token)))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("13. 모든 사용자 조회 성공 테스트")
    void _13_get_all_user_info_test() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/api/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk());
    }
}