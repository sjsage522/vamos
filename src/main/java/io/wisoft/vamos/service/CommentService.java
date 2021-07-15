package io.wisoft.vamos.service;

import io.wisoft.vamos.common.exception.DataNotFoundException;
import io.wisoft.vamos.common.util.SecurityUtils;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.dto.comment.CommentApplyRequest;
import io.wisoft.vamos.dto.comment.CommentUpdateRequest;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CommentRepository;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment apply(Long boardId, CommentApplyRequest request) {
        User currentUser = findCurrentUser();
        Board targetBoard = findBoard(boardId);

        Comment newComment = Comment.from(currentUser, targetBoard, request.getContent());
        applyToParent(newComment, request.getParentId());

        return commentRepository.save(newComment);
    }

    //TODO 페이징
    @Transactional(readOnly = true)
    public List<Comment> findAllByBoardIdIfParentIsNull(Long boardId) {
        return commentRepository.findAllByBoardIdIfParentIsNull(boardId);
    }

    @Transactional
    public Comment update(Long commentId, CommentUpdateRequest request) {
        Comment updateComment = findCommentWithUser(commentId);
        User currentUser = findCurrentUser();

        checkValidation(updateComment.getUser(), currentUser);
        updateComment.updateContent(request.getContent());

        return updateComment;
    }

    @Transactional
    public void delete(Long commentId) {
        Comment deleteComment = findCommentWithUser(commentId);
        User currentUser = findCurrentUser();

        checkValidation(deleteComment.getUser(), currentUser);
        commentRepository.delete(deleteComment);
    }

    private void checkValidation(User target, User current) {
        if (!current.equals(target)) throw new IllegalStateException("다른 사용자의 답글입니다.");
    }

    private void applyToParent(Comment comment, Long parentId) {
        if (parentId != null) {
            Comment parentComment = findComment(parentId);
            parentComment.addChildComment(comment);
        }
    }

    private Comment findCommentWithUser(Long commentId) {
        return commentRepository.findByIdWithUser(commentId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 답글 입니다."));

    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 답글입니다."));
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
