package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.board.BoardStatus;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.UploadFileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 서비스 테스트")
class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    UploadFileRepository uploadFileRepository;

    @Test
    @DisplayName("게시글 업로드 테스트 (첨부이미지가 없는 경우)")
    void board_upload_not_include_files_test() {
        //given
        Board board = mock(Board.class);
        UploadFile uploadFile = mock(UploadFile.class);

        given(boardRepository.save(board))
                .willReturn(board);
        List<UploadFile> uploadFiles = List.of(uploadFile);

        //when
        boardService.upload(board, null);

        //then
        then(boardRepository).should(times(1)).save(board);
        then(uploadFileRepository).should(times(0)).saveAll(uploadFiles);
        assertThat(board.getUploadFiles().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 업로드 테스트 (첨부이미지가 있는 경우)")
    void board_upload_include_files_test() throws IOException {
        //given
        Board board = mock(Board.class);
        FileInputStream fis = new FileInputStream("/Users/jun/Downloads/penguin.png");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "files",
                "penguin.png",
                "image/png",
                fis);
        MultipartFile[] multipartFiles = {multipartFile};

        given(boardRepository.save(board))
                .willReturn(board);
        given(uploadFileRepository.saveAll(any()))
                .willReturn(any());

        //when
        boardService.upload(board, multipartFiles);

        //then
        then(boardRepository).should(times(1)).save(board);
        then(uploadFileRepository).should(times(1)).saveAll(any());
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 (다른 사용자의 게시글은 수정할 수 없음)")
    void board_update_failed_test() {
        //given
        User user = mock(User.class);
        Board board = mock(Board.class);

        given(boardRepository.findById(1L))
                .willReturn(java.util.Optional.of(board));
        given(board.getUser())
                .willReturn(user);
        setField(user, "username", "writer");
        setField(user, "nickname", "writer22");
        System.out.println(user);
        System.out.println(user.getNickname());

        User otherUser = mock(User.class);
        Board otherBoard = mock(Board.class);

        given(otherBoard.getUser())
                .willReturn(otherUser);
        setField(otherUser, "username", "writer");
        setField(otherUser, "nickname", "writer22");


        //then
        assertThrows(IllegalStateException.class,
                () -> boardService.update(1L, otherBoard));
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void board_update_success_test() {
        //given
        User user = mock(User.class);
        setField(user, "username", "writer");
        setField(user, "nickname", "writer22");
        Board board = mock(Board.class, Mockito.RETURNS_DEEP_STUBS);
        setField(board, "id", 1L);
        setField(board, "user", user);
        setField(board, "status", BoardStatus.SALE);

        System.out.println(board.getId());

        given(boardRepository.findById(1L))
                .willReturn(java.util.Optional.of(board));


        //when
        board.changeStatus(BoardStatus.RESERVE);
        Board update = boardService.update(1L, board);

        //then
        assertThat(update.getStatus()).isEqualTo(BoardStatus.RESERVE);
    }
}