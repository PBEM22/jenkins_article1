package article1be.amazonS3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AmazonS3Service {

    private static final Logger log = LoggerFactory.getLogger(AmazonS3Service.class);
    private final AmazonS3Client client;
    @Value("${s3.bucket}")
    private String amazonS3Bucket;

    @Autowired
    public AmazonS3Service(AmazonS3Client client) {
        this.client = client;
    }

    public MetaData upload(MultipartFile image) throws IOException {
        MetaData metaData = new MetaData();

        // 업로드할 파일의 원래 이름을 가져옵니다.
        String originalFileName = image.getOriginalFilename();
        metaData.setOriginalFileName(originalFileName);

        // 파일 이름을 변경합니다.
        String fileName = changeFileName(originalFileName);
        metaData.setChangeFileName(fileName); // 변경된 파일 이름을 메타데이터에 저장합니다.

        // S3에 업로드할 파일의 메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metaData.setType(image.getContentType());
        metadata.setContentLength(image.getSize());
        metaData.setUrl(client.getUrl(amazonS3Bucket, fileName).toString());

        // S3에 파일 업로드
        client.putObject(amazonS3Bucket, fileName, image.getInputStream(), metadata);

        // 로그 출력 (원래 파일 이름과 변경된 파일 이름)
        log.info("origin name = " + metaData.getOriginalFileName());
        log.info("changed name = " + metaData.getChangeFileName());
        log.info("url = " + metaData.getUrl());

        // 업로드한 파일의 S3 URL 주소 반환
        return metaData;
    }

    private String changeFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + extension; // 타임스탬프와 UUID를 이용한 고유 파일 이름

        return newFileName;
    }

    @Getter
    @Setter
    public static class MetaData {
        private String originalFileName;
        private String changeFileName;
        private String url;
        private String type;
    }
}
