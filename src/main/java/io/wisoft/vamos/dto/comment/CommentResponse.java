package io.wisoft.vamos.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.comment.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"parentId", "commentId", "boardId", "username", "content", "children"})
@ApiModel("답글 공통 응답")
public class CommentResponse {

    @ApiModelProperty(
            value = "부모 답글 고유 id",
            name = "parent_id",
            example = "1"
    )
    @JsonProperty("parent_id")
    private Long parentId;

    @ApiModelProperty(
            value = "답글 고유 id",
            name = "comment_id",
            example = "2"
    )
    @JsonProperty("comment_id")
    private Long commentId;

    @ApiModelProperty(
            value = "게시글 고유 id",
            name = "parentId",
            example = "1"
    )
    @JsonProperty("board_id")
    private Long boardId;

    @ApiModelProperty(
            value = "사용자명",
            name = "writer",
            example = "tester"
    )
    @JsonProperty("writer")
    private String username;

    @ApiModelProperty(
            value = "답글 내용",
            name = "content",
            example = "content"
    )
    @JsonProperty("content")
    private String content;

    @ApiModelProperty(
            value = "자식 답글들",
            name = "children"
    )
    @JsonProperty("children")
    private final List<CommentResponse> children = new ArrayList<>();

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
    }
}
