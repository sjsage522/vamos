package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.board.BoardStatus;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.exception.NoMatchBoardInfoException;
import io.wisoft.vamos.exception.NoMatchCategoryInfoException;
import io.wisoft.vamos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.util.FileUtils.saveFilesOnDisc;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UploadFileRepository uploadFileRepository;
    private final CommentRepository commentRepository;
    private static final int MAX_FILE_LENGTH = 5;

    @Transactional
    public Board upload(BoardUploadRequest boardUploadRequest, MultipartFile[] files) {

        if (files != null && files.length > MAX_FILE_LENGTH)
            throw new IllegalArgumentException("이미지 갯수는 5개를 초과할 수 없습니다.");
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

    @Transactional(readOnly = true)
    public List<Board> findByEarthDistance() {
        User user = findCurrentUser();

        final UserLocation location = user.getLocation();
        final Double x = location.getX(); //longitude (경도)
        final Double y = location.getY(); //latitude (위도)

        //TODO 페이징, EX) user.getRadius() 를 통해 사용자가 지정한 범위에 따라 조회하는 기능
        final int radius = 2000; //2km
        return boardRepository.findByEarthDistance(x, y, radius);
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(NoMatchBoardInfoException::new);
    }

    @Transactional
    public Board update(Long boardId, BoardUploadRequest request) {
        Board modifiedBoard = getBoard(request);
        Board updateBoard = findById(boardId);

        compareUser(updateBoard.getUser(), modifiedBoard.getUser());

        updateBoard.updateBoard(modifiedBoard);
        return updateBoard;
    }

    @Transactional
    public void delete(Long boardId) {
        User currentUser = findCurrentUser();
        Board deleteBoard = findById(boardId);

        compareUser(deleteBoard.getUser(), currentUser);

        deleteComments(boardId);
        boardRepository.delete(deleteBoard);
    }

    private void deleteComments(Long boardId) {
        List<Long> commentIds = commentRepository.findAllByBoardId(boardId).stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        commentRepository.deleteWithIds(commentIds);
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

    private void compareUser(User target, User current) {
        if (!current.equals(target)) throw new NoMatchBoardInfoException("다른 사용자의 게시글입니다.");
    }

    private User findCurrentUser() {
//        String username = getCurrentUsername();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 사용자 입니다."));
        return null;
    }

    private Category getCategory(BoardUploadRequest boardUploadRequest) {
        Category instance = Category.of(boardUploadRequest.getCategoryNameEN());
        return categoryRepository.findByName(instance.getName())
                .orElseThrow(NoMatchCategoryInfoException::new);
    }
}
