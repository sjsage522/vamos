package io.wisoft.vamos.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.comment.CommentApplyRequest;
import io.wisoft.vamos.dto.comment.CommentResponse;
import io.wisoft.vamos.dto.comment.CommentUpdateRequest;
import io.wisoft.vamos.security.UserPrincipal;
import io.wisoft.vamos.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.api.ApiResult.*;

@RequiredArgsConstructor
@Api(tags = "CommentController")
@RestController
@RequestMapping("api")
public class CommentController {

    private final CommentService commentService;

    /**
     * 답글 작성
     * @param boardId 게시글 고유 id
     * @param request dto
     * @return comment info
     */
    @ApiOperation(value = "답글 작성", notes = "특정 게시글에 답글을 작성합니다. 부모 답글에 답글을 작성할 수 있습니다.")
    @ApiImplicitParam(name = "boardId", value = "게시글 고유 id", example = "1", required = true, dataTypeClass = Long.class, paramType = "path")
    @PostMapping("/comment/board/{boardId}")
    public ApiResult<CommentResponse> applyComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentApplyRequest request,
            @ApiIgnore UserPrincipal userPrincipal) {
        return succeed(
                getCommentResponse(
                        commentService.apply(boardId, request, userPrincipal)
                )
        );
    }

    /**
     * 답글의 부모 id가 null 인,
     * 특정 게시글의 모든 답글 조회
     * @param boardId 게시글 고유 id
     * @return comment info
     */
    @ApiOperation(value = "답글 리스트 조회", notes = "특정 게시글의 모든 답글들을 조회합니다.")
    @ApiImplicitParam(name = "boardId", value = "게시글 고유 id", example = "1", required = true, dataTypeClass = Long.class, paramType = "path")
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
    @ApiOperation(value = "답글 수정", notes = "답글을 수정 합니다.")
    @ApiImplicitParam(name = "commentId", value = "답글 고유 id", example = "1", required = true, dataTypeClass = Long.class, paramType = "path")
    @PatchMapping("/comment/{commentId}")
    public ApiResult<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request,
            @ApiIgnore UserPrincipal userPrincipal) {
        return succeed(
                new CommentResponse(
                        commentService.update(commentId, request, userPrincipal)
                )
        );
    }

    /**
     * 답글 삭제
     * @param commentId 답글 고유 id
     * @return comment info
     */
    @ApiOperation(value = "답글 삭제", notes = "답글을 삭제 합니다. 연관된 자식 답글들 또한 삭제됩니다.")
    @ApiImplicitParam(name = "commentId", value = "답글 고유 id", example = "1", required = true, dataTypeClass = Long.class, paramType = "path")
    @DeleteMapping("/comment/{commentId}")
    public ApiResult<String> deleteComment(
            @PathVariable Long commentId,
            @ApiIgnore UserPrincipal userPrincipal) {
        commentService.delete(commentId, userPrincipal);
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
