package io.wisoft.vamos.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Upload;
import io.wisoft.vamos.config.property.AmazonS3Property;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final AmazonS3Property amazonS3Property;

    public UploadFileResponse upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("can not convert multipartFile to file."));

        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName(); //S3 save name
        String uploadImageUrl = putS3(uploadFile, fileName); //upload
        removeNewFile(uploadFile);

        return new UploadFileResponse(
                uploadImageUrl,
                multipartFile.getOriginalFilename(),
                fileName,
                multipartFile.getSize(),
                FilenameUtils.getExtension(multipartFile.getOriginalFilename())
        );
    }

    private String putS3(File uploadFile, String fileName) {
        String bucket = amazonS3Property
                .getAws()
                .getS3()
                .getBucket();

        amazonS3Client.putObject(new PutObjectRequest(
                bucket,
                fileName,
                uploadFile
        ).withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File target) {
        if (target.delete()) {
            log.info("File delete success.");
            return;
        }
        throw new IllegalStateException("File delete fail");
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(
                System.getProperty("user.dir") +
                        "/" +
                        multipartFile.getOriginalFilename());

        if (convertFile.createNewFile()) {
            //try-with-resource
            try(FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
                fileOutputStream.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    @Getter
    @RequiredArgsConstructor
    public static class UploadFileResponse {
        private final String uploadImageUrl;
        private final String originalFileName;
        private final String storedFileName;
        private final long fileSize;
        private final String fileType;
    }
}
