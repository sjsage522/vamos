package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.Role;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CategoryRepository;
import io.wisoft.vamos.repository.UploadFileRepository;
import io.wisoft.vamos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 서비스 테스트")
class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    UploadFileRepository uploadFileRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    FileService fileService;

    @Test
    @DisplayName("게시글 업로드 테스트 (첨부이미지가 없는 경우)")
    void board_upload_not_include_files_test() {
        //given
        BoardUploadRequest boardUploadRequest = getBoardUploadRequest("title", "content", 1000, "DIGITAL_DEVICE");

        UploadFile uploadFile = mock(UploadFile.class);

        String username = "testUser";
        String email = "junseok@gmail.com";
        User user = getUser(username, email);

        Category category = getCategory(boardUploadRequest);

        Board board = getBoard(user, category, "title", "content", 1000);

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));
        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.of(category));
        given(boardRepository.save(any()))
                .willReturn(board);

        List<UploadFile> uploadFiles = List.of(uploadFile);

        //when
        boardService.upload(boardUploadRequest, null, email);

        //then
        then(uploadFileRepository).should(times(0)).saveAll(uploadFiles);
        assertThat(board.getUploadFiles().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 업로드 테스트 (첨부이미지가 있는 경우)")
    void board_upload_include_files_test() throws IOException {
        //given
        BoardUploadRequest boardUploadRequest = getBoardUploadRequest("title", "content", 1000, "DIGITAL_DEVICE");

        String username = "testUser";
        String email = "junseok@gmail.com";
        User user = getUser(username, email);

        Category category = getCategory(boardUploadRequest);

        Board board = getBoard(user, category, "title", "content", 1000);
        FileInputStream fis = new FileInputStream("/Users/jun/Downloads/penguin.png");

        MockMultipartFile multipartFile = new MockMultipartFile(
                "files",
                "penguin.png",
                "image/png",
                fis);
        MultipartFile[] multipartFiles = {multipartFile};

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));
        given(boardRepository.save(any()))
                .willReturn(board);
        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.of(category));

        //when
        boardService.upload(boardUploadRequest, multipartFiles, email);

        //then
        then(fileService).should(times(1)).uploadFiles(board, multipartFiles);
    }

    private Category getCategory(BoardUploadRequest boardUploadRequest) {
        return Category.of(boardUploadRequest.getCategoryNameEN());
    }

    private User getUser(String username, String email) {
        return User.builder()
                .username(username)
                .email(email)
                .phoneNumber(PhoneNumber.of("01012345678"))
                .nickname("tester")
                .location(UserLocation.from(0., 0., "test location"))
                .role(Role.USER)
                .build();
    }

    private Board getBoard(User user, Category category, String title, String content, int price) {
        return Board.from(title, content, price, user, category);
    }

    private BoardUploadRequest getBoardUploadRequest(String title, String content, int price, String categoryNameEN) {
        BoardUploadRequest boardUploadRequest = new BoardUploadRequest();
        boardUploadRequest.setTitle(title);
        boardUploadRequest.setContent(content);
        boardUploadRequest.setPrice(price);
        boardUploadRequest.setCategoryNameEN(categoryNameEN);
        return boardUploadRequest;
    }
}