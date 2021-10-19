package io.wisoft.vamos.dto.uploadfile;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("파일 공통 응답")
public class UploadFileResponse {

    @ApiModelProperty(
            value = "파일 이미지 고유 id",
            name = "id",
            example = "1"
    )
    private Long id;

    @ApiModelProperty(
            value = "파일 다운로드 경로",
            name = "file_download_uri"
    )
    @JsonProperty("file_download_uri")
    private String fileDownloadUrl;

    @ApiModelProperty(
            value = "원본 파일 이름",
            name = "original_file_name"
    )
    @JsonProperty("original_file_name")
    private String originalFileName;

    @ApiModelProperty(
            value = "저장된 파일 이름",
            name = "stored_file_name"
    )
    @JsonProperty("stored_file_name")
    private String storedFileName;

    @ApiModelProperty(
            value = "파일 형식",
            name = "extension",
            example = "png"
    )
    @JsonProperty("extension")
    private String fileType;

    @ApiModelProperty(
            value = "파일 크기",
            name = "file_size"
    )
    @JsonProperty("file_size")
    private Long fileSize;

    public UploadFileResponse(UploadFile uploadFile) {
        this.id = uploadFile.getId();
        this.fileDownloadUrl = uploadFile.getFileDownloadUri();
        this.originalFileName = uploadFile.getOriginalFileName();
        this.storedFileName = uploadFile.getStoredFileName();
        this.fileType = uploadFile.getFileType();
        this.fileSize = uploadFile.getFileSize();
    }
}
