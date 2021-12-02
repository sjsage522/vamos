package io.wisoft.vamos.domain.chatting;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chatting_room")
@Getter
@SequenceGenerator(
        name = "chatting_room_sequence_generator",
        sequenceName = "chatting_room_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class ChattingRoom extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chatting_room_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.REMOVE)
    private List<ChattingContent> chattingContents = new ArrayList<>();

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ChattingRoomStatus category;

    @Builder
    private ChattingRoom(User buyer, Board board, List<ChattingContent> chattingContents, ChattingRoomStatus category) {
        this.seller = board.getUser();
        this.buyer = buyer;
        this.board = board;
        this.chattingContents = chattingContents;
        this.category = category;
    }

    public void updateChatContent(List<ChattingContent> chattingContentList) {
        chattingContentList.forEach(content -> content.registerChatRoom(this));
    }

    protected ChattingRoom() {

    }

    @Override
    public String toString() {
        return "ChattingRoom{" +
                "id=" + id +
                ", seller=" + seller.getId() +
                ", buyer=" + buyer.getId() +
                ", board=" + board.getId() +
                ", chattingContents=" + chattingContents.size() +
                ", category=" + category.toString() +
                '}';
    }
}
