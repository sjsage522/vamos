package io.wisoft.vamos.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
        String storedFileName = UUID.randomUUID() + multipartFile.getName();
        String fileName = dirName + "/" + storedFileName; //S3 save name
        String uploadImageUrl = putS3(multipartFile, fileName); //upload

        return new UploadFileResponse(
                uploadImageUrl,
                multipartFile.getOriginalFilename(),
                storedFileName,
                multipartFile.getSize(),
                FilenameUtils.getExtension(multipartFile.getOriginalFilename())
        );
    }

    private String putS3(MultipartFile uploadFile, String fileName) throws IOException {
        String bucket = amazonS3Property
                .getAws()
                .getS3()
                .getBucket();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadFile.getContentType());
        amazonS3Client.putObject(new PutObjectRequest(
                bucket,
                fileName,
                uploadFile.getInputStream(),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
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
