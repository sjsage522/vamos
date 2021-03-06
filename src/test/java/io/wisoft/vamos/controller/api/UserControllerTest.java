package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.config.UserPrincipalMethodArgumentResolver;
import io.wisoft.vamos.domain.user.Role;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.repository.UserRepository;
import io.wisoft.vamos.security.oauth2.AuthProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static io.wisoft.vamos.util.JsonUtils.toJson;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("사용자 API 통합 테스트")
class UserControllerTest {

    private MockMvc mockMvc;


    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    private User saveUser;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new UserPrincipalMethodArgumentResolver())
                .build();
    }

    @BeforeTransaction
    void signup() {
        String username = "junseok";
        String email = "junseok@gmail.com";
        User user = User.builder()
                .username(username)
                .email(email)
                .role(Role.USER)
                .provider(AuthProvider.local)
                .build();
        saveUser = userRepository.save(user);
    }

    @AfterTransaction
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @WithUserDetails(value = "junseok@gmail.com", userDetailsServiceBeanName = "customUserDetailsService")
    @DisplayName("사용자 단건 조회 성공 테스트")
    void getUser_succeed_test() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                get("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("name", saveUser.getUsername())
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", is(saveUser.getUsername())))
                .andExpect(jsonPath("$.data.email", is(saveUser.getEmail())))
                .andExpect(jsonPath("$.error").doesNotExist())
        ;
    }

    @Test
    @Transactional
    @WithUserDetails(value = "junseok@gmail.com", userDetailsServiceBeanName = "customUserDetailsService")
    @DisplayName("사용자 위치정보 수정 성공 테스트")
    void userLocationUpdate_succeed_test() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                patch("/api/user/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                toJson(
                                        new HashMap<>() {
                                            {
                                                put("x", 1.);
                                                put("y", 1.);
                                                put("addressName", "update location");
                                            }
                                        }
                                )
                        )
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", is("junseok")))
                .andExpect(jsonPath("$.data.email", is(saveUser.getEmail())))
                .andExpect(jsonPath("$.data.location.x", is(1.)))
                .andExpect(jsonPath("$.error").doesNotExist())
        ;
    }
}