package io.wisoft.vamos.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static java.util.regex.Pattern.*;

@Embeddable
@Getter
@ApiModel("사용자 연락처 공통 응답")
public class PhoneNumber {

    @ApiModelProperty(
            value = "사용자 연락처",
            name = "value",
            example = "01012345678"
    )
    @Column(name = "phone_number", unique = true)
    @JsonProperty("value")
    private String phoneNumber;

    protected PhoneNumber() { /* empty */}

    private PhoneNumber(String phoneNumber) {
        checkArgument(checkPhoneNumber(phoneNumber), "유효하지 않은 번호입니다.");
        this.phoneNumber = phoneNumber;
    }

    public static PhoneNumber of(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        return matches("^\\d{2,3}\\d{3,4}\\d{4}$", phoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(getPhoneNumber(), that.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhoneNumber());
    }
}
