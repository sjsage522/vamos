package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    Optional<User> findByPhoneNumber(PhoneNumber phoneNumber);

    @Query("select count(u) " +
            "from User u " +
            "where u.username = :username " +
            "or u.phoneNumber = :phoneNumber " +
            "or u.nickname = :nickname")
    int findDuplicateUserCount(String username, PhoneNumber phoneNumber, String nickname);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
