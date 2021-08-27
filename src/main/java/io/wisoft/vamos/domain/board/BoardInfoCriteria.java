package io.wisoft.vamos.domain.board;

import lombok.Builder;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
public class BoardInfoCriteria {

    private Long id;

    private String title;

    private String content;

    private int price;

    /* package-private extends */
    protected BoardInfoCriteria() {/* empty */}

    @Builder
    public BoardInfoCriteria(Long id, String title, String content, int price) {
        checkArgument(id != null, "id must be provided");
        checkArgument(title != null, "id must be provided");
        checkArgument(content != null, "id must be provided");
        checkArgument(price >= 0, "price must be positive");
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
    }

    @Override
    public String toString() {
        return "BoardInfoCriteria{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", price=" + price +
                '}';
    }
}
