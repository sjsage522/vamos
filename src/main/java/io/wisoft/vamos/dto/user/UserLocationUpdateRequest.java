package io.wisoft.vamos.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@ApiModel("사용자 위치정보 수정 요청")
public class UserLocationUpdateRequest {

    @ApiModelProperty(
            value = "경도",
            name = "x",
            example = "0.0",
            required = true
    )
    private Double x;

    @ApiModelProperty(
            value = "위도",
            name = "y",
            example = "0.0",
            required = true
    )
    private Double y;

    @ApiModelProperty(
            value = "주소명",
            name = "address",
            example = "test address",
            required = true
    )
    private String addressName;

    protected UserLocationUpdateRequest() {}

    @Builder
    private UserLocationUpdateRequest(Double x, Double y, String addressName) {
        this.x = x;
        this.y = y;
        this.addressName = addressName;
    }
}
