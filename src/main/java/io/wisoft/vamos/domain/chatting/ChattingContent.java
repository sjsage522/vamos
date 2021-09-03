package io.wisoft.vamos.domain.chatting;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "chatting_content")
@Getter
@SequenceGenerator(
        name = "chatting_content_sequence_generator",
        sequenceName = "chatting_content_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class ChattingContent extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chatting_content_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean isRead;

    @Builder
    private ChattingContent(User writer, ChattingRoom chattingRoom, String content, boolean isRead) {
        this.writer = writer;
        this.chattingRoom = chattingRoom;
        this.content = content;
        this.isRead = isRead;
    }

    public void registerChatRoom(ChattingRoom chattingRoom) {
        this.chattingRoom = chattingRoom;
    }

    protected ChattingContent() {

    }
}
