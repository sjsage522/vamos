package io.wisoft.vamos.dto.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.board.BoardLocation;
import io.wisoft.vamos.domain.board.BoardStatus;
import io.wisoft.vamos.dto.category.CategoryResponse;
import io.wisoft.vamos.dto.uploadfile.UploadFileResponse;
import io.wisoft.vamos.dto.user.UserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class BoardResponse {
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("price")
    private int price;

    @JsonProperty("user_info")
    private UserResponse user;

    @JsonProperty("category_info")
    private CategoryResponse category;

    @JsonProperty("location")
    private BoardLocation boardLocation;

    @JsonProperty("status")
    private BoardStatus boardStatus;

    @JsonProperty("photos")
    private final List<UploadFileResponse> uploadFiles;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.price = board.getPrice();
        this.user = new UserResponse(board.getUser());
        this.category = new CategoryResponse(board.getCategory());
        this.boardLocation = board.getLocation();
        this.boardStatus = board.getStatus();
        this.uploadFiles = board.getUploadFiles().stream()
                .map(UploadFileResponse::new)
                .collect(Collectors.toList());
    }
}

