package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.User;
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
//        User findUser = findByUsername(username);
//        User currentUser = findCurrentUser();
//
//        compareUser(findUser, currentUser);
//
//        UserLocation location = UserLocation.from(request.getX(), request.getY(), request.getAddressName());
//        findUser.changeUserLocation(location);
//
//        return findUser;
        return null;
    }

    private void deleteBoards(Long userId) {
        List<Long> boardIds = boardRepository.findAllByUserId(userId).stream()
                .map(Board::getId)
                .collect(toList());

        for (Long boardId : boardIds) {
            deleteComments(boardId);
            deleteFiles(boardId);
        }
        boardRepository.deleteWithIds(boardIds);
    }

    private void deleteComments(Long boardId) {
        List<Long> commentIds = commentRepository.findAllByBoardId(boardId).stream()
                .map(Comment::getId)
                .collect(toList());
        commentRepository.deleteWithIds(commentIds);
    }

    private void deleteFiles(Long boardId) {
        List<Long> fileIds = uploadFileRepository.findAllByBoardId(boardId).stream()
                .map(UploadFile::getId)
                .collect(toList());
        uploadFileRepository.deleteWithIds(fileIds);
    }

    private void compareUser(User target, User current) {
        if (!current.equals(target)) throw new IllegalStateException("다른 사용자의 정보입니다.");
    }
}
