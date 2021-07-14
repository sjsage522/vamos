package io.wisoft.vamos.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.wisoft.vamos.domain.comment.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"parentId", "commentId", "boardId", "username", "content", "children"})
public class CommentResponse {

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("board_id")
    private Long boardId;

    @JsonProperty("writer")
    private String username;

    @JsonProperty("content")
    private String content;

    @JsonProperty("children")
    private final List<CommentResponse> children = new ArrayList<>();

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
    }
}
