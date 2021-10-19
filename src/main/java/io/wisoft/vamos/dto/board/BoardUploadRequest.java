package io.wisoft.vamos.dto.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.config.validation.FileArrayLength;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("게시글 업로드 요청")
public class BoardUploadRequest {

    @ApiModelProperty(
            value = "게시글 제목",
            name = "title",
            example = "title",
            required = true
    )
    @NotBlank
    private String title;

    @ApiModelProperty(
            value = "게시글 내용",
            name = "content",
            example = "content",
            required = true
    )
    @NotBlank
    private String content;

    @ApiModelProperty(
            value = "거래 가격",
            name = "price",
            example = "1000",
            required = true
    )
    @Range(min = 0, max = Integer.MAX_VALUE)
    private int price;

    @ApiModelProperty(
            value = "카테고리 고유 번호",
            name = "categoryNumber",
            example = "1",
            required = true
    )
    @NotNull
    private Long categoryNumber;

    @ApiModelProperty(
            value = "게시글 첨부 이미지",
            name = "files"
    )
    @FileArrayLength(message = "파일의 갯수가 유효한지 확인해 주세요.")
    private MultipartFile[] files;

    public BoardUploadRequest() {}

    @Builder
    private BoardUploadRequest(String title, String content, int price, Long categoryNumber, MultipartFile[] files) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.categoryNumber = categoryNumber;
        this.files = files;
    }

    @Override
    public String toString() {
        return "BoardUploadRequest{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", price=" + price +
                ", categoryNumber=" + categoryNumber +
                ", files.size=" + files.length +
                '}';
    }
}

