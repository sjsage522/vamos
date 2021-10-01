package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.dto.comment.CommentApplyRequest;
import io.wisoft.vamos.dto.comment.CommentUpdateRequest;
import io.wisoft.vamos.exception.NoMatchBoardInfoException;
import io.wisoft.vamos.exception.NoMatchCommentInfoException;
import io.wisoft.vamos.exception.NoMatchUserInfoException;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CommentRepository;
import io.wisoft.vamos.repository.UserRepository;
import io.wisoft.vamos.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.wisoft.vamos.util.UserUtils.compareUser;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment apply(Long boardId, CommentApplyRequest request, UserPrincipal currentUser) {
        User user = findUserByEmail(currentUser.getEmail());
        Board targetBoard = findBoard(boardId);

        Comment newComment = Comment.from(user, targetBoard, request.getContent());
        applyToParent(newComment, request.getParentId());

        return commentRepository.save(newComment);
    }

    //TODO 페이징
    @Transactional(readOnly = true)
    public List<Comment> findAllByBoardIdIfParentIsNull(Long boardId) {
        return commentRepository.findAllByBoardIdIfParentIsNull(boardId);
    }

    @Transactional
    public Comment update(Long commentId, CommentUpdateRequest request, UserPrincipal currentUser) {
        Comment target = findCommentWithUser(commentId);
        User user = findUserByEmail(currentUser.getEmail());

        compareUser(target.getUser(), user, "다른 사용자의 답글 입니다.");
        target.updateContent(request.getContent());

        return target;
    }

    @Transactional
    public void delete(Long commentId, UserPrincipal currentUser) {
        Comment target = findCommentWithUser(commentId);
        User user = findUserByEmail(currentUser.getEmail());

        compareUser(target.getUser(), user, "다른 사용자의 답글 입니다.");
        commentRepository.delete(target);
    }

    private void applyToParent(Comment comment, Long parentId) {
        if (parentId != null) {
            Comment parentComment = findComment(parentId);
            parentComment.addChildComment(comment);
        }
    }

    private Comment findCommentWithUser(Long commentId) {
        return commentRepository.findByIdWithUser(commentId)
                .orElseThrow(NoMatchCommentInfoException::new);

    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NoMatchCommentInfoException::new);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NoMatchUserInfoException::new);
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(NoMatchBoardInfoException::new);
    }
}
