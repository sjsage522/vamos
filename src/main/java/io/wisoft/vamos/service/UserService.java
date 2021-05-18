package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.exception.DataAlreadyExistsException;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User join(PhoneNumber phoneNumber, String nickName) {
        User joinUser = User.from(phoneNumber, nickName);

        if (findByPhoneNumber(phoneNumber).isPresent() || findByNickName(nickName).isPresent())
            throw new DataAlreadyExistsException("이미 존재하는 사용자 입니다.");

        return userRepository.save(joinUser);
    }

    public Optional<User> findByPhoneNumber(PhoneNumber phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<User> findByNickName(String nickName) {
        return userRepository.findByNickName(nickName);
    }
}
