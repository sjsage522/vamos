package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.exception.DataAlreadyExistsException;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(User newUser) {
        if (0 < userRepository.findDuplicateUserCount(newUser.getUserId(), newUser.getPhoneNumber(), newUser.getNickName()))
            throw new DataAlreadyExistsException("이미 존재하는 사용자 입니다.");

        newUser.setEncodedPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }
}
