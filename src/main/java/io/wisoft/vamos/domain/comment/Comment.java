package io.wisoft.vamos.domain.comment;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

@Entity
@Table(name = "comment")
@Getter
@SequenceGenerator(
        name = "comment_sequence_generator",
        sequenceName = "comment_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name = "comment")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence_generator")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private final List<Comment> children = new ArrayList<>();

    public void addChildComment(Comment child) {
        this.children.add(child);
        child.setParent(this);
    }

    private void setParent(Comment parent) {
        this.parent = parent;
    }

    protected Comment() {}

    private Comment(User user, Board board, String content) {
        checkArgument(user != null, "user must not null");
        checkArgument(board != null, "board must not null");
        checkArgument(isContentValid(content), "content is not valid");
        this.user = user;
        this.board = board;
        this.content = content;
    }

    public static Comment from(User user, Board board, String content) {
        return new Comment(user, board, content);
    }

    private boolean isContentValid(String content) {
        return content != null && !content.isBlank();
    }
}
