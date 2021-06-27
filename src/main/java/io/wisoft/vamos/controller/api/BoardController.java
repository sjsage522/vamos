package io.wisoft.vamos.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.board.BoardStatus;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.category.CategoryName;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.repository.CategoryRepository;
import io.wisoft.vamos.service.BoardService;
import io.wisoft.vamos.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static io.wisoft.vamos.controller.api.ApiResult.*;
import static io.wisoft.vamos.controller.api.UserController.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    @PostMapping("/board")
    public ApiResult<BoardResponse> uploadBoard(@Valid @RequestBody BoardUploadRequest boardUploadRequest) {
        Board uploadBoard = getBoard(boardUploadRequest);
        return succeed(new BoardResponse(
                boardService.upload(uploadBoard)
                )
        );
    }

    private Board getBoard(BoardUploadRequest boardUploadRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Category category = getCategory(boardUploadRequest);

        Board board = Board.from(
                boardUploadRequest.getTitle(),
                boardUploadRequest.getContent(),
                boardUploadRequest.getPrice(),
                user,
                category
        );
        board.changeStatus(BoardStatus.SALE);

        return board;
    }

    private Category getCategory(BoardUploadRequest boardUploadRequest) {
        return categoryRepository.findByName(boardUploadRequest.getCategoryNameEN())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다."));
    }

    @Getter
    private static class BoardUploadRequest {

        @NotBlank(message = "제목을 입력해 주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해 주세요.")
        private String content;

        @Min(value = 1, message = "가격은 0보다 커야합니다.")
        private int price;

        private CategoryName categoryNameEN;
    }

    @Getter
    private static class BoardResponse {

        @JsonProperty("title")
        private String title;

        @JsonProperty("content")
        private String content;

        @JsonProperty("price")
        private int price;

        @JsonProperty("user_info")
        private UserResponse user;

        @JsonProperty("category_info")
        private CategoryController.CategoryResponse category;

        @JsonProperty("status")
        private BoardStatus boardStatus;

        @JsonProperty("photos")
        private List<UploadPhotoController.UploadPhotoResponse> uploadPhotos;

        public BoardResponse(Board board) {
            this.title = board.getTitle();
            this.content = board.getContent();
            this.price = board.getPrice();
            this.user = new UserResponse(board.getUser());
            this.category = new CategoryController.CategoryResponse(board.getCategory());
            this.boardStatus = board.getStatus();
        }
    }
}
