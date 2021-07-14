package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.dto.ApiResult;
import io.wisoft.vamos.dto.comment.CommentApplyRequest;
import io.wisoft.vamos.dto.comment.CommentResponse;
import io.wisoft.vamos.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.ApiResult.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/board/{boardId}")
    public ApiResult<CommentResponse> applyComment(
            @PathVariable Long boardId,
            @RequestBody CommentApplyRequest request) {
        return succeed(
                getCommentResponse(
                        commentService.apply(boardId, request)
                )
        );
    }

    @GetMapping("/comments/board/{boardId}")
    public ApiResult<List<CommentResponse>> allComments(@PathVariable Long boardId) {
        return succeed(commentService.findAllByBoardIdIfParentIsNull(boardId)
                .stream()
                .map(this::getCommentResponse)
                .collect(Collectors.toList())
        );
    }

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
