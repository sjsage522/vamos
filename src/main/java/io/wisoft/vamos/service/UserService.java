package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.exception.NoMatchUserInfoException;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CommentRepository;
import io.wisoft.vamos.repository.UploadFileRepository;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UploadFileRepository uploadFileRepository;


    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                NoMatchUserInfoException::new
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
        UserLocation location = UserLocation.from(request.getX(), request.getY(), request.getAddressName());
        findUser.changeUserLocation(location);
        return findUser;
    }
}
