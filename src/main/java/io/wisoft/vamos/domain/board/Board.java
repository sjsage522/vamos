package io.wisoft.vamos.domain.board;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

@Entity
@Table(name = "board")
@Getter
@SequenceGenerator(
        name = "board_sequence_generator",
        sequenceName = "board_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Board extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "board_sequence_generator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Embedded
    private BoardLocation location;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BoardStatus status;     /* default -> SALE */

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) /* owner 가 아닌쪽을 정의 */
    private final List<UploadFile> uploadFiles = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private final List<ChattingRoom> chattingRooms = new ArrayList<>();

    protected Board() {}

    @Builder
    private Board(String title, String content, int price, User user, Category category) {
        checkArgument(checkStringValid(title, content), "제목이나 내용은 비워둘 수 없습니다.");
        checkArgument(checkPriceValid(price), "가격은 0 보다 커야합니다.");
        checkArgument(checkObjectValid(user, category), "사용자 또는 카테고리가 유효하지 않습니다.");
        UserLocation userLocation = user.getLocation();
        checkArgument(userLocation != null, "사용자 위치 정보가 설정되어 있지 않습니다.");
        this.location = BoardLocation.from(userLocation.getX(), userLocation.getY(), userLocation.getAddressName());
        this.title = title;
        this.content = content;
        this.price = price;
        this.user = user;
        this.category = category;
        this.status = BoardStatus.SALE;
    }

    public static Board from(String title, String content, int price, User user, Category category) {
        return new Board(title, content, price, user, category);
    }

    public void changeStatus(BoardStatus status) {
        this.status = status;
    }

    public void updateBoard(Board modifiedBoard) {
        this.title = modifiedBoard.getTitle();
        this.content = modifiedBoard.getContent();
        this.price = modifiedBoard.getPrice();
        this.category = modifiedBoard.getCategory();
    }

    private boolean checkStringValid(String... values) {
        return Arrays.stream(values)
                .noneMatch(value -> value == null || value.isBlank());
    }

    private boolean checkPriceValid(int price) {
        return price > 0;
    }

    private boolean checkObjectValid(Object... objects) {
        return Arrays.stream(objects)
                .noneMatch(Objects::isNull);
    }

    /* 응답 데이터 설정 */
    public void addFiles(List<UploadFile> uploadFiles) {
        this.uploadFiles.addAll(uploadFiles);
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", price=" + price +
                ", user=" + user.getEmail()+
                ", category=" + category.getName().getEn() +
                ", status=" + status +
                ", uploadFiles=" + uploadFiles +
                '}';
    }
}
