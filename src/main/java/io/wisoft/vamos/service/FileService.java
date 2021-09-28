package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.repository.UploadFileRepository;
import io.wisoft.vamos.util.S3Uploader;
import io.wisoft.vamos.util.S3Uploader.UploadFileResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileService {

    private final S3Uploader s3Uploader;
    private final UploadFileRepository uploadFileRepository;

    @Transactional
    public List<UploadFile> uploadFiles(Board target, MultipartFile[] multipartFiles) {
        final List<UploadFile> result = Arrays.stream(multipartFiles)
                .map(multipartFile -> {
                    try {
                        if (!checkImageMineType(multipartFile))
                            throw new IllegalArgumentException("올바른 파일 형식이 아닙니다.");
                        UploadFileResponse fileResponse;
                        fileResponse = s3Uploader.upload(multipartFile, "static");
                        return UploadFile
                                .builder()
                                .board(target)
                                .originalFileName(fileResponse.getOriginalFileName())
                                .storedFileName(fileResponse.getStoredFileName())
                                .fileSize(fileResponse.getFileSize())
                                .fileDownloadUri(fileResponse.getUploadImageUrl())
                                .fileType(fileResponse.getFileType())
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .collect(Collectors.toList());

        //persist
        uploadFileRepository.saveAll(result);
        target.addFiles(result);

        return result;
    }

    @Transactional
    public void deleteAllByBoardId(Long boardId) {
        uploadFileRepository.deleteAllByBoardId(boardId);
    }

    private boolean checkImageMineType(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();

        Tika tika = new Tika();

        String mineType = tika.detect(inputStream);

        return mineType.startsWith("image");
    }
}
