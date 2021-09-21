package io.wisoft.vamos.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.wisoft.vamos.config.property.AmazonS3Property;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    private final AmazonS3Property amazonS3Property;

    public AmazonS3Config(AmazonS3Property amazonS3Property) {
        this.amazonS3Property = amazonS3Property;
    }

    @Bean
    public AmazonS3Client amazonS3Client() {
        AmazonS3Property.Aws aws = amazonS3Property
                .getAws();

        AmazonS3Property.Aws.Credentials credentials = aws
                .getCredentials();

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                credentials.getAccessKey(),
                credentials.getSecretKey()
        );

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(aws.getRegion().toString())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
