package io.wisoft.vamos.domain.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class BoardLocation {

    private Double x;
    private Double y;

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
