package io.wisoft.vamos.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@ApiModel("사용자 공통 응답")
public class UserResponse {

    @ApiModelProperty(
            value = "사용자 고유 id",
            name = "id",
            example = "1"
    )
    private Long id;

    @ApiModelProperty(
            value = "사용자명",
            name = "username",
            example = "tester"
    )
    @JsonProperty("username")
    private String username;

    @ApiModelProperty(
            value = "사용자 이메일",
            name = "email",
            example = "tester@gmail.com"
    )
    @JsonProperty("email")
    private String email;

    @ApiModelProperty(
            value = "사용자 닉네임",
            name = "nickname",
            example = "tester"
    )
    @JsonProperty("nickname")
    private String nickname;

    @ApiModelProperty(
            value = "사용자 연락처",
            name = "phoneNumber"
    )
    @JsonProperty("phone_number")
    private PhoneNumber phoneNumber;

    @ApiModelProperty(
            value = "사용자 위치정보",
            name = "location"
    )
    @JsonProperty("location")
    private UserLocation location;

    @ApiModelProperty(
            value = "사용자 프로필",
            name = "profile"
    )
    @JsonProperty("profile")
    private String picture;

    public UserResponse(User source) {
        BeanUtils.copyProperties(source, this);
    }
}
