package io.wisoft.vamos.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.repository.UploadFileRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UploadFileController {

    private final UploadFileRepository uploadFileRepository;

    @GetMapping("/image/{fileId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("fileId") Long fileId) throws IOException {

        UploadFile findFile = uploadFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다."));

        String uploadPath = Paths.get(findFile.getFileDownloadUri()).toString();
        String storedFileName = findFile.getStoredFileName();
        String filePath = uploadPath + "/" + storedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            byte[] binaryFile = IOUtils.toByteArray(inputStream);
            return new ResponseEntity<>(binaryFile, headers, HttpStatus.OK);
        }
    }

    @Getter
    protected static class UploadFileResponse {

        private Long id;

        @JsonProperty("original_file_name")
        private String originalFileName;

        @JsonProperty("stored_file_name")
        private String storedFileName;

        @JsonProperty("file_path")
        private String fileDownloadUri;

        @JsonProperty("extension")
        private String fileType;

        @JsonProperty("file_size")
        private Long fileSize;

        public UploadFileResponse(UploadFile uploadFile) {
            this.id = uploadFile.getId();
            this.originalFileName = uploadFile.getOriginalFileName();
            this.storedFileName = uploadFile.getStoredFileName();
            this.fileDownloadUri = uploadFile.getFileDownloadUri();
            this.fileType = uploadFile.getFileType();
            this.fileSize = uploadFile.getFileSize();
        }
    }
}
