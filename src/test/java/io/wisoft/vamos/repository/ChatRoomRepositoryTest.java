package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.chatting.ChattingContent;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import io.wisoft.vamos.domain.chatting.ChattingRoomStatus;
import io.wisoft.vamos.domain.user.Role;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.security.oauth2.AuthProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.List;

@Disabled
@DataJpaTest
class ChatRoomRepositoryTest {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatContentRepository chatContentRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    public ChatRoomRepositoryTest(BoardRepository boardRepository,
                               CategoryRepository categoryRepository,
                               UserRepository userRepository,
                               ChatRoomRepository chatRoomRepository,
                               ChatContentRepository chatContentRepository) {
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatContentRepository = chatContentRepository;
    }

    @BeforeEach
    void init() {
        User user = User.builder()
                .email("test@email.com")
                .nickname("tester")
                .username("tester")
                .password("1234")
                .provider(AuthProvider.local)
                .location(UserLocation.from(0., 0., "test location"))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Category category = categoryRepository.findById(1L).get();

        Board board = Board.builder()
                .title("title")
                .content("content")
                .price(100)
                .category(category)
                .user(user)
                .build();
        boardRepository.save(board);

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .buyer(user)
                .board(board)
                .category(ChattingRoomStatus.NORMAL)
                .build();
        chatRoomRepository.save(chattingRoom);

        ChattingContent chattingContent = ChattingContent.builder()
                .writer(user)
                .chattingRoom(chattingRoom)
                .content("chatting content")
                .isRead(false)
                .build();
        chatContentRepository.save(chattingContent);

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("게시글 id로 채팅 내용과 함께 채팅방 리스트 조회")
    @Test
    void findChattingRoomByBoardId() {
        final List<ChattingRoom> chattingRooms = chatRoomRepository.findChattingRoomWithContentsByBoardId(1L);

        Assertions.assertThat(chattingRooms.size()).isEqualTo(1);
        Assertions.assertThat(chattingRooms.get(0).getChattingContents().size()).isEqualTo(1);
    }
}