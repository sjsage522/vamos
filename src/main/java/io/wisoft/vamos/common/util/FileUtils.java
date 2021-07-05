package io.wisoft.vamos.common.util;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.uploadphoto.UploadFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileUtils {

    private static final String TODAY = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    private static final String FILE_UPLOAD_PATH = System.getenv("FILE_UPLOAD_PATH") + TODAY;

    private static String getRandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static List<UploadFile> saveFilesOnDisc(Board board, MultipartFile[] saveFiles) {
        if (saveFiles == null) return Collections.emptyList();

        List<UploadFile> fileList = new ArrayList<>();
        File dir = new File(FILE_UPLOAD_PATH);
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile saveFile : saveFiles) {
            try {
                /* 파일에 따른 확장자 얻기 */
                String extension = FilenameUtils.getExtension(saveFile.getOriginalFilename());

                /* disc 에 저장할 파일명 */
                String saveName = getRandomString() + "." + extension;

                /* disc 에 saveName 이름을 갖는 파일 생성 */
                File dest = new File(FILE_UPLOAD_PATH, saveName);
                saveFile.transferTo(dest);

                if (!checkExtensionValidation(dest))
                    throw new IllegalArgumentException("올바른 파일형식이 아닙니다.");

                /* file entity 객체 생성 */
                UploadFile uploadFile = UploadFile
                        .builder()
                        .board(board)
                        .originalFileName(saveFile.getOriginalFilename())
                        .storedFileName(saveName)
                        .fileSize(saveFile.getSize())
                        .fileDownloadUri(FILE_UPLOAD_PATH)
                        .fileType(extension)
                        .build();

                fileList.add(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileList;
    }

    private static boolean checkExtensionValidation(File file) throws IOException {

        String[] PERMISSION_FILE_MIME_TYPE = {
                "image/jpeg",
                "image/png"
        };

        String mimeType = new Tika().detect(file);
        return Arrays.asList(PERMISSION_FILE_MIME_TYPE).contains(mimeType);
    }
}
