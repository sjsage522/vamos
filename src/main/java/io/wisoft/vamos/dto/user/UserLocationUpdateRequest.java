package io.wisoft.vamos.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLocationUpdateRequest {

    private Double x;
    private Double y;
    private String addressName;
}
