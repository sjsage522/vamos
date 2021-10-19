package io.wisoft.vamos.dto.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("게시글 공통 응답")
public class BoardResponse {

    @ApiModelProperty(
            value = "게시글 고유 id",
            name = "id",
            example = "1"
    )
    private Long id;

    @ApiModelProperty(
            value = "게시글 제목",
            name = "title",
            example = "title"
    )
    @JsonProperty("title")
    private String title;

    @ApiModelProperty(
            value = "게시글 내용",
            name = "content",
            example = "content"
    )
    @JsonProperty("content")
    private String content;

    @ApiModelProperty(
            value = "거래 가격",
            name = "price",
            example = "1000"
    )
    @JsonProperty("price")
    private int price;

    @ApiModelProperty(
            value = "게시자 정보",
            name = "user"
    )
    @JsonProperty("user_info")
    private UserResponse user;

    @ApiModelProperty(
            value = "카테고리 정보",
            name = "category"
    )
    @JsonProperty("category_info")
    private CategoryResponse category;

    @ApiModelProperty(
            value = "게시글 위치",
            name = "location"
    )
    @JsonProperty("location")
    private BoardLocation boardLocation;

    @ApiModelProperty(
            value = "게시글 상태",
            name = "status",
            example = "sale"
    )
    @JsonProperty("status")
    private BoardStatus boardStatus;

    @ApiModelProperty(
            value = "게시글 첨부 이미지들",
            name = "photos"
    )
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

