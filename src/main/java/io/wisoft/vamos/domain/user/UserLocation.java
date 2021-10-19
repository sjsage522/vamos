package io.wisoft.vamos.domain.user;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@ApiModel("사용자 위치정보 공통 응답")
public class UserLocation {

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
            name = "address_name",
            example = "test address"
    )
    @Column(name = "address_name")
    private String addressName;

    protected UserLocation() {}

    private UserLocation(Double x, Double y, String addressName) {
        Preconditions.checkArgument(isValid(addressName), "올바른 지역 명칭이 아닙니다.");
        this.x = x;
        this.y = y;
        this.addressName = addressName;
    }

    public static UserLocation from(Double x, Double y, String addressName) {
        return new UserLocation(x, y, addressName);
    }

    private boolean isValid(String addressName) {
        return !addressName.isBlank();
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "x=" + x +
                ", y=" + y +
                ", addressName='" + addressName + '\'' +
                '}';
    }
}
