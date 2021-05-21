package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickName(String nickName);

    Optional<User> findByPhoneNumber(PhoneNumber phoneNumber);

    @Query("select count(u) " +
            "from User u " +
            "where u.userId = :userId " +
            "or u.phoneNumber = :phoneNumber " +
            "or u.nickName = :nickName")
    int findDuplicateUserCount(String userId, PhoneNumber phoneNumber, String nickName);
}
