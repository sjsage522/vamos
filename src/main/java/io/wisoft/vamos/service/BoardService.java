package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.comment.Comment;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.exception.NoMatchBoardInfoException;
import io.wisoft.vamos.exception.NoMatchCategoryInfoException;
import io.wisoft.vamos.exception.NoMatchUserInfoException;
import io.wisoft.vamos.exception.NotYetSettingUserLocationException;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.CategoryRepository;
import io.wisoft.vamos.repository.CommentRepository;
import io.wisoft.vamos.repository.UserRepository;
import io.wisoft.vamos.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;

    private static final int MAX_FILE_LENGTH = 5;

    @Transactional
    public Board upload(BoardUploadRequest boardUploadRequest, MultipartFile[] files, String email) {

        if (files != null && files.length > MAX_FILE_LENGTH)
            throw new IllegalArgumentException("이미지 갯수는 5개를 초과할 수 없습니다.");

        Board uploadBoard = getBoard(boardUploadRequest, email);
        Board saveBoard = boardRepository.save(uploadBoard);

        if (files != null && files.length > 0)
            fileService.uploadFiles(saveBoard, files);

        return saveBoard;
    }

    @Transactional(readOnly = true)
    public List<Board> findByEarthDistance(String email) {
        User user = findCurrentUser(email);

        UserLocation location = UserUtils.getUserLocation(user)
                .orElseThrow(NotYetSettingUserLocationException::new);
        Double x = location.getX(); //longitude (경도)
        Double y = location.getY(); //latitude (위도)
        //TODO 페이징, EX) user.getRadius() 를 통해 사용자가 지정한 범위에 따라 조회하는 기능
        final int radius = 2000; //2km
        return boardRepository.findByEarthDistance(x, y, radius);
    }

    @Transactional(readOnly = true)
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(NoMatchBoardInfoException::new);
    }

    @Transactional
    public Board update(Long boardId, BoardUploadRequest request, MultipartFile[] files, String email) {
        Board modifiedBoard = getBoard(request, email);
        Board updateBoard = findById(boardId);

        compareUser(updateBoard.getUser(), modifiedBoard.getUser());

        updateBoard.updateBoard(modifiedBoard);

        fileService.deleteAllByBoardId(boardId);
        if (files != null && files.length > 0)
            fileService.uploadFiles(updateBoard, files);

        return updateBoard;
    }

    @Transactional
    public void delete(Long boardId, String email) {
        User currentUser = findCurrentUser(email);
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

    private Board getBoard(BoardUploadRequest boardUploadRequest, String email) {
        User user = findCurrentUser(email);
        Category category = getCategory(boardUploadRequest);

        return Board.from(
                boardUploadRequest.getTitle(),
                boardUploadRequest.getContent(),
                boardUploadRequest.getPrice(),
                user,
                category
        );
    }

    private void compareUser(User target, User current) {
        if (!current.equals(target)) throw new NoMatchBoardInfoException("다른 사용자의 게시글입니다.");
    }

    private User findCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NoMatchUserInfoException::new);
    }

    private Category getCategory(BoardUploadRequest boardUploadRequest) {
        Category instance = Category.of(boardUploadRequest.getCategoryNameEN());
        return categoryRepository.findByName(instance.getName())
                .orElseThrow(NoMatchCategoryInfoException::new);
    }
}
