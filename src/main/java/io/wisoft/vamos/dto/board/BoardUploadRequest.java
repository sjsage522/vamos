package io.wisoft.vamos.dto.board;

import io.wisoft.vamos.domain.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUploadRequest {
    private String title;
    private String content;
    private int price;
    private String categoryNameEN;

    public BoardUploadRequest() {}
    public BoardUploadRequest(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.price = board.getPrice();
        this.categoryNameEN = board.getCategory()
                .getName()
                .getEn();
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

