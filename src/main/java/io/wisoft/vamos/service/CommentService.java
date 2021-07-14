package io.wisoft.vamos.service;

import io.wisoft.vamos.common.exception.DataNotFoundException;
import io.wisoft.vamos.common.util.SecurityUtils;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.dto.comment.CommentApplyRequest;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CommentRepository;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment apply(Long boardId, CommentApplyRequest request) {
        User findUser = findCurrentUser();
        Board findBoard = findBoard(boardId);

        Comment comment = Comment.from(findUser, findBoard, request.getContent());
        applyToParent(comment, request.getParentId());

        return commentRepository.save(comment);
    }

    //TODO 페이징
    public List<Comment> findAllByBoardIdIfParentIsNull(Long boardId) {
        return commentRepository.findAllByBoardIdIfParentIsNull(boardId);
    }

    private void applyToParent(Comment comment, Long parentId) {
        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new DataNotFoundException("존재하지 않는 부모답글입니다."));
            parentComment.addChildComment(comment);
        }
    }

    private User findCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 게시글입니다."));
    }
}
