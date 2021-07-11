package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.dto.ApiResult;
import io.wisoft.vamos.dto.board.BoardResponse;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.ApiResult.succeed;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public ApiResult<BoardResponse> uploadBoard(
            @ModelAttribute BoardUploadRequest boardUploadRequest,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        return succeed(new BoardResponse(
                        boardService.upload(boardUploadRequest, files)
                )
        );
    }

    //TODO 사용자 반경 몇 키로미터 내의 게시글들 조회, 페이징
    @GetMapping("/boards")
    public ApiResult<List<BoardResponse>> getBoardsByEarthDistance() {
        List<Board> boards = boardService.findByEarthDistance();

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
        return succeed(new BoardResponse(boardService.update(boardId, request)));
    }


    //TODO DELETE BOARD
    //@DeleteMapping("/board/{boardId}")
}
