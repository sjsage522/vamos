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
    public ApiResult<BoardResponse> boardUpload(
            @ModelAttribute BoardUploadRequest boardUploadRequest,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        return succeed(new BoardResponse(
                        boardService.upload(boardUploadRequest, files)
                )
        );
    }

    //TODO 사용자 반경 몇 키로미터 내의 게시글들 조회, 페이징
    @GetMapping("/boards")
    public ApiResult<List<BoardResponse>> getBoardListByEarthDistance() {
        List<Board> boards = boardService.findByEarthDistance();

        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());

        return succeed(boardResponses);
    }

    @GetMapping("/board/{boardId}")
    public ApiResult<BoardResponse> getBoard(@PathVariable Long boardId) {
        return succeed(
                new BoardResponse(
                        boardService.findById(boardId)
                )
        );
    }

    @PatchMapping("/board/{boardId}")
    public ApiResult<BoardResponse> boardUpdate(
            @PathVariable Long boardId,
            @ModelAttribute BoardUploadRequest request) {
        return succeed(
                new BoardResponse(
                        boardService.update(boardId, request)
                )
        );
    }


    /**
     * TODO DELETE BOARD
     * 게시글을 삭제하면 게시글에 달려있는 댓글들과 파일들이 먼저 삭제되어야 한다. (참조)
     * 게시글 내의 답글들을 삭제할 떄는 게시글에 물려있는 모든 답글들을 in 절로 삭제한다.
     * 첨부파일의 경우에는 cascade 옵션을 통해 삭제한다.
     */
    @DeleteMapping("/board/{boardId}")
    public ApiResult<String> boardDelete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return succeed("board is deleted successfully");
    }
}
