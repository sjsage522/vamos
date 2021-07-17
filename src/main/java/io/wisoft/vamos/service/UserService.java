package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.common.exception.DataAlreadyExistsException;
import io.wisoft.vamos.common.exception.DataNotFoundException;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.user.UserJoinRequest;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static io.wisoft.vamos.common.util.SecurityUtils.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(UserJoinRequest request) {
        User newUser = getUser(request);

        if (0 < userRepository.findDuplicateUserCount(newUser.getUsername(), newUser.getPhoneNumber(), newUser.getNickname()))
            throw new DataAlreadyExistsException("이미 존재하는 사용자 입니다.");

        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new DataNotFoundException("존재하지 않는 사용자 입니다.")
        );
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        //TODO 페이징 처리
        return userRepository.findAll();
    }

    @Transactional
    public User updateUserLocation(String username, UserLocationUpdateRequest request) {
        User findUser = findByUsername(username);
        User currentUser = findCurrentUser();

        compareUser(findUser, currentUser);

        UserLocation location = UserLocation.from(request.getX(), request.getY(), request.getAddressName());
        findUser.changeUserLocation(location);

        return findUser;
    }

    @Transactional
    public void delete(String username) {

    }

    private void compareUser(User target, User current) {
        if (!current.equals(target)) throw new IllegalStateException("다른 사용자의 정보입니다.");
    }

    private User findCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 사용자 입니다."));
    }

    private User getUser(UserJoinRequest request) {

        UserLocation location = UserLocation.from(request.getX(), request.getY(), request.getAddressName());

        final User user = User.from(
                request.getUsername(),
                request.getPassword(),
                PhoneNumber.of(request.getPhoneNumber()),
                request.getNickname(),
                location
        );
        settingUser(user);

        return user;
    }

    /**
     * 사용자 권한 설정과 비밀번호 암호화
     */
    private void settingUser(User user) {
        user.setAuthority(Collections.singleton(Authority.of("ROLE_USER")));
        user.setEncodedPassword(passwordEncoder.encode(user.getPassword()));
    }
}
