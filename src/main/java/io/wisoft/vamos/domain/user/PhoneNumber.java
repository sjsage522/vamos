package io.wisoft.vamos.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.google.common.base.Preconditions.*;
import static java.util.regex.Pattern.*;

@Embeddable
@Getter
public class PhoneNumber {

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
}
