package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.user.MMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MMSRepository extends JpaRepository<MMS, Long> {

    Optional<MMS> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
