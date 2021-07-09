package io.wisoft.vamos.dto.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUploadRequest {
    private String title;
    private String content;
    private int price;
    private String categoryNameEN;
}

