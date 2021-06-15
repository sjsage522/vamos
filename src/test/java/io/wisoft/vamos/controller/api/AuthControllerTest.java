package io.wisoft.vamos.controller.api;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;

import static io.wisoft.vamos.util.JsonUtils.toJson;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("인증 API 테스트")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    private static final String value = "01053402457";
    private static String certification;

    @BeforeEach
    void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("01. 문자 메시지 인증번호 요청 테스트")
    void _01_sms_verification_request_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/sms-certification/sends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<>() {
                                            {
                                                put("phoneNumber", value);
                                            }
                                        }
                                )
                        )
        );

        String response = result
                .andReturn()
                .getResponse()
                .getContentAsString();

        int certificationIndex = response.indexOf("certification");

        String body = response.substring(certificationIndex);

        certification = body.split("\"")[2].trim();

        logger.info("certification = {}", certification);

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", is("인증번호를 성공적으로 요청했습니다.")))
                .andExpect(jsonPath("$.data.certification", is(certification)))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }

    @Test
    @DisplayName("02. 인증번호 검증 요청 테스트")
    void _02_sms_verification_check_test() throws Exception {

        ResultActions result = mockMvc.perform(
                post("/api/sms-certification/confirms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<>() {
                                            {
                                                put("from", value);
                                                put("certification", certification);
                                            }
                                        }
                                )
                        )
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.body", is("인증에 성공했습니다.")))
                .andExpect(jsonPath("$.error_message").doesNotExist())
        ;
    }
}