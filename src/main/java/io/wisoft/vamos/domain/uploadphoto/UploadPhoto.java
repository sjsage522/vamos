package io.wisoft.vamos.domain.uploadphoto;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.board.Board;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "upload_photo")
@Getter
@SequenceGenerator(
        name = "upload_photo_sequence_generator",
        sequenceName = "upload_photo_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class UploadPhoto extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "upload_photo_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "original_file_name")
    private String OriginalFileName;

    @Column(name = "stored_file_name")
    private String StoredFileName;
}
