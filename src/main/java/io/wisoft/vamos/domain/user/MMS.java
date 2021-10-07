package io.wisoft.vamos.domain.user;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Entity
@Table(name = "mms")
@Getter
@SequenceGenerator(
        name = "mms_sequence_generator",
        sequenceName = "mms_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class MMS extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mms_sequence_generator")
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "certification_number", nullable = false)
    private String certificationNumber;

    @Column(name = "checked")
    private boolean checked;

    protected MMS() {}

    private MMS(String phoneNumber, String certificationNumber) {
        checkArgument(!isBlank(phoneNumber), "phoneNumber must be provide.");
        checkArgument(!isBlank(certificationNumber), "certification number must be provide.");
        this.phoneNumber = phoneNumber;
        this.certificationNumber = certificationNumber;
    }

    public static MMS from(String phoneNumber, String certificationNumber) {
        return new MMS(phoneNumber, certificationNumber);
    }
}
