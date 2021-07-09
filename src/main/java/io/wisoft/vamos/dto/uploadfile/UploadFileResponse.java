package io.wisoft.vamos.dto.uploadfile;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileResponse {

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
