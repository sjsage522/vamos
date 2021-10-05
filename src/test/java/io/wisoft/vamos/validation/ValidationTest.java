package io.wisoft.vamos.validation;

import io.wisoft.vamos.dto.board.BoardUploadRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationTest {

    @Test
    void FileArrayLengthValidation() throws IOException {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        final List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            final String fileName = "testFile:" + i;
            final String contentType = "sql";
            final String filePath = "src/test/resources/import.sql";
            final MultipartFile file = getMockMultipartFile(fileName, contentType, filePath);
            multipartFileList.add(file);
        }

        final MultipartFile[] files = multipartFileList.toArray(new MultipartFile[6]);


        final BoardUploadRequest request = BoardUploadRequest.builder()
                .title("title")
                .content("content")
                .price(100)
                .categoryNameEN("ETC")
                .files(files)
                .build();

        final Set<ConstraintViolation<BoardUploadRequest>> violations = validator.validate(request);
        for (ConstraintViolation<BoardUploadRequest> violation : violations) {
//            System.out.println("violation = " + violation);
//            System.out.println("violation.getMessage() = " + violation.getMessage());
            Assertions.assertThat(violation.getMessage()).isEqualTo("파일의 갯수가 유효한지 확인해 주세요.");
        }
    }

    @Test
    void boardUploadRequestValidation() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        final BoardUploadRequest request = BoardUploadRequest.builder()
                .title("")
                .content("")
                .price(-1)
                .categoryNameEN("")
                .build();

        final Set<ConstraintViolation<BoardUploadRequest>> violations = validator.validate(request);
        for (ConstraintViolation<BoardUploadRequest> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}

