package io.wisoft.vamos.domain.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@ApiModel("게시글 위치 공통 응답")
public class BoardLocation {

    @ApiModelProperty(
            value = "경도",
            name = "x",
            example = "0.0"
    )
    private Double x;

    @ApiModelProperty(
            value = "위도",
            name = "y",
            example = "0.0"
    )
    private Double y;

    @ApiModelProperty(
            value = "주소명",
            name = "addressName",
            example = "test address"
    )
    @JsonProperty("address_name")
    private String addressName;

    protected BoardLocation() {}

    private BoardLocation(Double x, Double y, String addressName) {
        Preconditions.checkArgument(isValid(addressName), "올바른 지역 명칭이 아닙니다.");
        this.x = x;
        this.y = y;
        this.addressName = addressName;
    }

    public static BoardLocation from(Double x, Double y, String addressName) {
        return new BoardLocation(x, y, addressName);
    }

    private boolean isValid(String addressName) {
        return !addressName.isBlank();
    }
}
