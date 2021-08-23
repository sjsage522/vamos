package io.wisoft.vamos.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserLocationUpdateRequest {

    private Double x;
    private Double y;
    private String addressName;

    protected UserLocationUpdateRequest() {}

    @Builder
    private UserLocationUpdateRequest(Double x, Double y, String addressName) {
        this.x = x;
        this.y = y;
        this.addressName = addressName;
    }
}
