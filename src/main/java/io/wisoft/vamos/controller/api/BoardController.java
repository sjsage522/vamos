package io.wisoft.vamos.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.board.BoardStatus;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.repository.CategoryRepository;
import io.wisoft.vamos.service.BoardService;
import io.wisoft.vamos.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.common.util.SecurityUtils.getCurrentUsername;
import static io.wisoft.vamos.controller.api.ApiResult.succeed;
import static io.wisoft.vamos.controller.api.UserController.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private static final int MAX_FILE_LENGTH = 5;

    @PostMapping("/board")
    public ApiResult<BoardResponse> uploadBoard(
            @ModelAttribute BoardUploadRequest boardUploadRequest,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        log.info("files = {}", Arrays.toString(files));
        if (files.length > MAX_FILE_LENGTH) throw new IllegalArgumentException("이미지 갯수는 5개를 초과할 수 없습니다.");
        Board uploadBoard = getBoard(boardUploadRequest);
        return succeed(new BoardResponse(
                        boardService.upload(uploadBoard, files)
                )
        );
    }

    //TODO 사용자 반경 몇 키로미터 내의 게시글들 조회, 페이징
    @GetMapping("/boards")
    public ApiResult<List<BoardResponse>> getBoardsByEarthDistance() {
        User user = findCurrentUser();
        List<Board> boards = boardService.findByEarthDistance(user);

        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());

        return succeed(boardResponses);
    }

    @GetMapping("/board/{boardId}")
    public ApiResult<BoardResponse> boardInfo(@PathVariable Long boardId) {
        return succeed(new BoardResponse(boardService.findById(boardId)));
    }

    @PatchMapping("/board/{boardId}")
    public ApiResult<BoardResponse> boardUpdate(
            @PathVariable Long boardId,
            @ModelAttribute BoardUploadRequest request) {
        Board updateBoard = getBoard(request);
        return succeed(new BoardResponse(boardService.update(boardId, updateBoard)));
    }


    //TODO DELETE BOARD
    //@DeleteMapping("/board/{boardId}")

    private Board getBoard(BoardUploadRequest boardUploadRequest) {
        User user = findCurrentUser();
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
        Category instance = Category.of(boardUploadRequest.getCategoryNameEN());
        return categoryRepository.findByName(instance.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다."));
    }

    private User findCurrentUser() {
        String username = getCurrentUsername();
        return userService.findByUsername(username);
    }

    @Getter
    @Setter
    @ToString
    private static class BoardUploadRequest {
        private String title;
        private String content;
        private int price;
        private String categoryNameEN;
    }

    @Getter
    private static class BoardResponse {
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
        private CategoryController.CategoryResponse category;

        @JsonProperty("status")
        private BoardStatus boardStatus;

        @JsonProperty("photos")
        private final List<UploadFileController.UploadFileResponse> uploadFiles;

        public BoardResponse(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.price = board.getPrice();
            this.user = new UserResponse(board.getUser());
            this.category = new CategoryController.CategoryResponse(board.getCategory());
            this.boardStatus = board.getStatus();
            this.uploadFiles = board.getUploadFiles().stream()
                    .map(UploadFileController.UploadFileResponse::new)
                    .collect(Collectors.toList());
        }
    }
}
