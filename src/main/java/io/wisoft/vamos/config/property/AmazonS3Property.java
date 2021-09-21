package io.wisoft.vamos.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.Name;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("cloud")
public class AmazonS3Property {

    private final Aws aws;

    @Getter
    @RequiredArgsConstructor
    public static final class Aws {
        private final Credentials credentials;
        private final S3 s3;
        private final Region region;

        @Getter
        @RequiredArgsConstructor
        public static final class Credentials {
            private final String accessKey;

            private final String secretKey;
        }

        @Getter
        @RequiredArgsConstructor
        public static final class S3 {
            private final String bucket;
        }

        @Getter
        public static final class Region {
            private final String staticValue;

            public Region(@Name("static") String staticValue) {
                this.staticValue = staticValue;
            }

            @Override
            public String toString() {
                return staticValue;
            }
        }
    }
}
