package io.wisoft.vamos.dto.board;

import io.wisoft.vamos.config.validation.FileArrayLength;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BoardUploadRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Range(min = 0, max = Integer.MAX_VALUE)
    private int price;

    @NotBlank
    private String categoryNameEN;

    @FileArrayLength(message = "파일의 갯수가 유효한지 확인해 주세요.")
    private MultipartFile[] files;

    public BoardUploadRequest() {}

    @Builder
    private BoardUploadRequest(String title, String content, int price, String categoryNameEN, MultipartFile[] files) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.categoryNameEN = categoryNameEN;
        this.files = files;
    }

    @Override
    public String toString() {
        return "BoardUploadRequest{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", price=" + price +
                ", categoryNameEN='" + categoryNameEN + '\'' +
                '}';
    }
}

