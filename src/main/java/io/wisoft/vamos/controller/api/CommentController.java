package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.comment.CommentApplyRequest;
import io.wisoft.vamos.dto.comment.CommentResponse;
import io.wisoft.vamos.dto.comment.CommentUpdateRequest;
import io.wisoft.vamos.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.api.ApiResult.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CommentController {

    private final CommentService commentService;

    /**
     * 게시글 작성
     * @param boardId 게시글 고유 id
     * @param request dto
     * @return comment info
     */
    @PostMapping("/comment/board/{boardId}")
    public ApiResult<CommentResponse> commentApply(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentApplyRequest request) {
        return succeed(
                getCommentResponse(
                        commentService.apply(boardId, request)
                )
        );
    }

    /**
     * 답글의 부모 id가 null 인,
     * 특정 게시글의 모든 답글 조회
     * @param boardId 게시글 고유 id
     * @return comment info
     */
    @GetMapping("/comments/board/{boardId}")
    public ApiResult<List<CommentResponse>> getCommentList(@PathVariable Long boardId) {
        return succeed(commentService.findAllByBoardIdIfParentIsNull(boardId)
                .stream()
                .map(this::getCommentResponse)
                .collect(Collectors.toList())
        );
    }

    /**
     * 답글 수정
     * @param commentId 답글 고유 id
     * @param request dto
     * @return comment info
     */
    @PatchMapping("/comment/{commentId}")
    public ApiResult<CommentResponse> commentUpdate(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request) {
        return succeed(
                new CommentResponse(
                        commentService.update(commentId, request)
                )
        );
    }

    /**
     * 답글 삭제
     * @param commentId 답글 고유 id
     * @return comment info
     */
    @DeleteMapping("/comment/{commentId}")
    public ApiResult<String> commentDelete(
            @PathVariable Long commentId) {
        commentService.delete(commentId);
        return succeed("comment is deleted successfully");
    }

    /**
     * CommentResponse DTO
     * 계층형 답글로 나타내기 위한 로직 포함
     * @param comment comment entity
     * @return CommentResponse DTO
     */
    private CommentResponse getCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse(comment);
        Comment parent = comment.getParent();

        if (parent != null) commentResponse.setParentId(parent.getId());

        List<Comment> children = comment.getChildren();

        if (!children.isEmpty()) {
            children.sort(Comparator.comparing(Comment::getId));
            children.stream()
                    .map(this::getCommentResponse)
                    .forEach(childResponse -> commentResponse.getChildren().add(childResponse));
        }
        return commentResponse;
    }
}
