package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.exception.DataAlreadyExistsException;
import io.wisoft.vamos.exception.DataNotFoundException;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(User newUser) {
        if (0 < userRepository.findDuplicateUserCount(newUser.getUsername(), newUser.getPhoneNumber(), newUser.getNickname()))
            throw new DataAlreadyExistsException("이미 존재하는 사용자 입니다.");

        newUser.setAuthority(Collections.singleton(Authority.of("ROLE_USER")));
        newUser.setEncodedPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new DataNotFoundException("존재하지 않는 사용자 입니다.")
        );
    }

    public List<User> findAll() {
        //TODO 페이징 처리
        return userRepository.findAll();
    }
}
