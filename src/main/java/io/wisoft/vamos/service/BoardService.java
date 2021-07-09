package io.wisoft.vamos.service;

import io.wisoft.vamos.common.exception.DataNotFoundException;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.board.BoardStatus;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CategoryRepository;
import io.wisoft.vamos.repository.UploadFileRepository;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static io.wisoft.vamos.common.util.FileUtils.saveFilesOnDisc;
import static io.wisoft.vamos.common.util.SecurityUtils.getCurrentUsername;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UploadFileRepository uploadFileRepository;
    private static final int MAX_FILE_LENGTH = 5;

    @Transactional
    public Board upload(BoardUploadRequest boardUploadRequest, MultipartFile[] files) throws IllegalArgumentException {

        if (files != null && files.length > MAX_FILE_LENGTH) throw new IllegalArgumentException("이미지 갯수는 5개를 초과할 수 없습니다.");
        Board uploadBoard = getBoard(boardUploadRequest);

        Board board = boardRepository.save(uploadBoard);

        List<UploadFile> fileList = saveFilesOnDisc(board, files);

        /* 게시글 첨부 이미지가 존재하는 경우 */
        if (!CollectionUtils.isEmpty(fileList)) {
            uploadFileRepository.saveAll(fileList);
            board.addFiles(fileList);
        }

        return board;
    }

    public List<Board> findByEarthDistance() {
        User user = findCurrentUser();

        final UserLocation location = user.getLocation();
        final Double x = location.getX(); //longitude (경도)
        final Double y = location.getY(); //latitude (위도)

        //TODO 페이징, EX) user.getRadius() 를 통해 사용자가 지정한 범위에 따라 조회하는 기능
        final int radius = 2000; //2km
        return boardRepository.findByEarthDistance(x, y, radius);
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 게시글 입니다."));
    }

    @Transactional
    public Board update(Long boardId, BoardUploadRequest request) {
        Board updateBoard = getBoard(request);
        Board findBoard = findById(boardId);
        User user = updateBoard.getUser();

        if (!user.equals(findBoard.getUser())) throw new IllegalStateException("다른 사용자의 게시글은 수정할 수 없습니다.");

        findBoard.updateBoard(updateBoard);
        return findBoard;
    }

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

    private User findCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 사용자 입니다."));
    }

    private Category getCategory(BoardUploadRequest boardUploadRequest) {
        Category instance = Category.of(boardUploadRequest.getCategoryNameEN());
        return categoryRepository.findByName(instance.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다."));
    }
}
