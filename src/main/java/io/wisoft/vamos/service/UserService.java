package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.exception.NoMatchUserInfoException;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                NoMatchUserInfoException::new
        );
    }

    @Transactional(readOnly = true)
    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                NoMatchUserInfoException::new
        );
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        //TODO 페이징 처리
        return userRepository.findAll();
    }

    @Transactional
    public User updateUserLocation(String email, UserLocationUpdateRequest request) {
        User findUser = findByEmail(email);
        UserLocation location = UserLocation.from(request.getX(), request.getY(), request.getAddressName());
        findUser.changeUserLocation(location);
        return findUser;
    }
}
