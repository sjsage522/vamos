package io.wisoft.vamos.controller.api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;

import static io.wisoft.vamos.util.JsonUtils.toJson;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("사용자 API 테스트")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    static String token;

    @Test
    @DisplayName("01. 사용자 회원 가입 테스트")
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
                                            }
                                        }
                                )
                        )
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(5)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.username", is("testId")))
                .andExpect(jsonPath("$.data.nickname", is("tester")))
                .andExpect(jsonPath("$.data.phone_number.value", is("01012345678")))
                .andExpect(jsonPath("$.data.user_roles[0].role_name", is("ROLE_USER")))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("02. 사용자 로그인 테스트")
    void _02_user_login_test() throws Exception {

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
    @DisplayName("03. 특정 사용자 조회 테스트")
    void _03_get_user_info_test() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/api/user?username=testId")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.username", is("testId")))
                .andExpect(jsonPath("$.data.nickname", is("tester")))
                .andExpect(jsonPath("$.data.phone_number.value", is("01012345678")))
                .andExpect(jsonPath("$.data.user_roles[0].role_name", is("ROLE_USER")))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }
}