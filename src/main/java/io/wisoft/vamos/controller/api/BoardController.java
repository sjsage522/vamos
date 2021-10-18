package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.board.BoardResponse;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.security.UserPrincipal;
import io.wisoft.vamos.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 업로드
     *
     * @param request dto
     * @return board info
     */
    @PostMapping("/board")
    public ApiResult<BoardResponse> uploadBoard(
            @ModelAttribute @Valid BoardUploadRequest request,
            UserPrincipal userPrincipal
    ) {
        return succeed(new BoardResponse(
                        boardService.upload(request, userPrincipal)
                )
        );
    }

    /**
     * 게시글 리스트 조회
     *
     * @return boardList info
     */
    //TODO 사용자 반경 몇 키로미터 내의 게시글들 조회, 페이징
    @GetMapping("/boards")
    public ApiResult<List<BoardResponse>> getBoardListByEarthDistance(UserPrincipal userPrincipal) {
        List<Board> boards = boardService.findByEarthDistance(userPrincipal);

        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());

        return succeed(boardResponses);
    }

    /**
     * 게시글 단건 조회
     *
     * @param boardId 게시글 고유 id
     * @return board info
     */
    @GetMapping("/board/{boardId}")
    public ApiResult<BoardResponse> getBoard(@PathVariable Long boardId) {
        return succeed(
                new BoardResponse(
                        boardService.findById(boardId)
                )
        );
    }

    /**
     * 게시글 수정
     *
     * @param boardId 게시글 고유 id
     * @param request dto
     * @return board info
     */
    @PatchMapping("/board/{boardId}")
    public ApiResult<BoardResponse> updateBoard(
            @PathVariable Long boardId,
            @ModelAttribute @Valid BoardUploadRequest request,
            UserPrincipal userPrincipal) {
        return succeed(
                new BoardResponse(
                        boardService.update(boardId, request, userPrincipal)
                )
        );
    }


    /**
     * 게시글을 삭제하면 게시글에 달려있는 댓글들과 파일들이 먼저 삭제되어야 한다. (참조)
     * 게시글 내의 답글들을 삭제할 떄는 게시글에 물려있는 모든 답글들을 in 절로 삭제한다.
     * 첨부파일의 경우에는 cascade 옵션을 통해 삭제한다.
     */
    @DeleteMapping("/board/{boardId}")
    public ApiResult<String> deleteBoard(@PathVariable Long boardId,
                                         UserPrincipal userPrincipal) {
        boardService.delete(boardId, userPrincipal);
        return succeed("board is deleted successfully");
    }
}
