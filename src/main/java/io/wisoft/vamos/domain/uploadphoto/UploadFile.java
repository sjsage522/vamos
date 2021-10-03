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
public class UploadFile extends BaseTimeEntity {

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
    private String originalFileName;

    @Column(name = "stored_file_name")
    private String storedFileName;

    @Column(name = "file_download_uri", length = 512)
    private String fileDownloadUri;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    protected UploadFile() {}

    private UploadFile(Board board, String originalFileName, String storedFileName, String fileDownloadUri, String fileType, Long fileSize) {
        this.board = board;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public static UploadFile.UploadFileBuilder builder() { return new UploadFile.UploadFileBuilder(); }

    public static class UploadFileBuilder {
        private Board board;
        private String originalFileName;
        private String storedFileName;
        private String fileDownloadUri;
        private String fileType;
        private Long fileSize;

        UploadFileBuilder() {}

        public UploadFile.UploadFileBuilder board(Board board) {
            this.board = board;
            return this;
        }

        public UploadFile.UploadFileBuilder originalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
            return this;
        }

        public UploadFile.UploadFileBuilder storedFileName(String storedFileName) {
            this.storedFileName = storedFileName;
            return this;
        }

        public UploadFile.UploadFileBuilder fileDownloadUri(String fileDownloadUri) {
            this.fileDownloadUri = fileDownloadUri;
            return this;
        }

        public UploadFile.UploadFileBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public UploadFile.UploadFileBuilder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public UploadFile build() {
            return new UploadFile(this.board, this.originalFileName, this.storedFileName, this.fileDownloadUri, this.fileType, this.fileSize);
        }
    }
}
