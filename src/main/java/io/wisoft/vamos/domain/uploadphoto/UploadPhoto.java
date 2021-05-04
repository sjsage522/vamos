package io.wisoft.vamos.domain.uploadphoto;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.board.Board;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "upload_photo")
@Getter
public class UploadPhoto extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "original_file_name")
    private String OriginalFileName;

    @Column(name = "stored_file_name")
    private String StoredFileName;
}
