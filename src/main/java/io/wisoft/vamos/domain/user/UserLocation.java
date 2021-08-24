package io.wisoft.vamos.domain.user;

import com.google.common.base.Preconditions;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class UserLocation {

    private Double x;
    private Double y;

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
