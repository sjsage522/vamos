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
import io.wisoft.vamos.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.util.UserUtils.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final ChatService chatService;

    @Transactional
    public Board upload(BoardUploadRequest boardUploadRequest, UserPrincipal currentUser) {
        Board uploadBoard = getBoard(boardUploadRequest, currentUser.getEmail());
        Board saveBoard = boardRepository.save(uploadBoard);

        MultipartFile[] files = boardUploadRequest.getFiles();
        if (files != null && files.length > 0)
            fileService.uploadFiles(saveBoard, files);

        return saveBoard;
    }

    @Transactional(readOnly = true)
    public List<Board> findByEarthDistance(UserPrincipal currentUser) {
        User user = findUserByEmail(currentUser.getEmail());

        UserLocation location = getUserLocation(user)
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
    public Board update(Long boardId, BoardUploadRequest boardUploadRequest, UserPrincipal currentUser) {
        Board modifiedBoard = getBoard(boardUploadRequest, currentUser.getEmail());
        Board target = findById(boardId);

        compareUser(target.getUser(), modifiedBoard.getUser(), "다른 사용자의 게시글 입니다.");

        target.updateBoard(modifiedBoard);

        fileService.deleteAllByBoardId(boardId);

        MultipartFile[] files = boardUploadRequest.getFiles();
        if (files != null && files.length > 0)
            fileService.uploadFiles(target, files);

        return target;
    }

    @Transactional
    public void delete(Long boardId, UserPrincipal currentUser) {
        User user = findUserByEmail(currentUser.getEmail());
        Board target = findById(boardId);

        compareUser(target.getUser(), user, "다른 사용자의 게시글 입니다.");

        deleteComments(boardId);
        chatService.deleteChatRoomsByBoardId(boardId);
        boardRepository.delete(target);
    }

    private void deleteComments(Long boardId) {
        List<Long> commentIds = commentRepository.findAllByBoardId(boardId).stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        commentRepository.deleteWithIds(commentIds);
    }

    private Board getBoard(BoardUploadRequest boardUploadRequest, String email) {
        User user = findUserByEmail(email);
        Category category = getCategory(boardUploadRequest);

        return Board.from(
                boardUploadRequest.getTitle(),
                boardUploadRequest.getContent(),
                boardUploadRequest.getPrice(),
                user,
                category
        );
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NoMatchUserInfoException::new);
    }

    private Category getCategory(BoardUploadRequest boardUploadRequest) {
        Category instance = Category.of(boardUploadRequest.getCategoryNumber());
        return categoryRepository.findByName(instance.getName())
                .orElseThrow(NoMatchCategoryInfoException::new);
    }
}
