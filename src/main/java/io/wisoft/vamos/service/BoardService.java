package io.wisoft.vamos.service;

import io.wisoft.vamos.common.exception.DataNotFoundException;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static io.wisoft.vamos.common.util.FileUtils.saveFilesOnDisc;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UploadFileRepository uploadFileRepository;

    @Transactional
    public Board upload(Board uploadBoard, MultipartFile[] files) throws IllegalArgumentException {
        Board board = boardRepository.save(uploadBoard);

        List<UploadFile> fileList = saveFilesOnDisc(board, files);

        /* 게시글 첨부 이미지가 존재하는 경우 */
        if (!CollectionUtils.isEmpty(fileList)) {
            uploadFileRepository.saveAll(fileList);
            board.addFiles(fileList);
        }

        return board;
    }

    public List<Board> findByEarthDistance(User user) {
        final UserLocation location = user.getLocation();
        final Double x = location.getX(); //longitude (경도)
        final Double y = location.getY(); //latitude (위도)

        //TODO 페이징, EX) user.getRadius() 를 통해 사용자가 지정한 범위에 따라 조회하는 기능
        final int radius = 2000; //2km
        return boardRepository.findByEarthDistance(x, y, radius);
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new DataNotFoundException("존재하지 않는 게시글 입니다."));
    }
}
