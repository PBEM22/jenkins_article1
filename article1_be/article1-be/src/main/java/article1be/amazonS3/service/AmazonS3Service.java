package article1be.amazonS3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AmazonS3Service {

    private final AmazonS3Client client;
    @Value("${s3.bucket}")
    private String amazonS3Bucket;

    @Autowired
    public AmazonS3Service(AmazonS3Client client) {
        this.client = client;
    }

    public MetaData upload(MultipartFile image) throws IOException {
        MetaData metaData = new MetaData();

        /* 업로드할 파일의 이름을 변경 */
        String originalFileName = image.getOriginalFilename();
        metaData.setOriginalFileName(originalFileName);
        String fileName = changeFileName(originalFileName);
        metaData.setChangeFileName(changeFileName(originalFileName));

        /* S3에 업로드할 파일의 메타데이터 생성 */
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metaData.setType(image.getContentType());
        metadata.setContentLength(image.getSize());

        /* S3에 파일 업로드 */
        client.putObject(amazonS3Bucket, fileName, image.getInputStream(), metadata);

        /* 업로드한 파일의 S3 URL 주소 반환 */
        return metaData;
    }

    private String changeFileName(String originalFileName) {
        /* 업로드할 파일의 이름을 변경하는 로직 */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return originalFileName + "_" + LocalDateTime.now().format(formatter);
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
