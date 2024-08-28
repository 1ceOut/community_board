package storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class NcpObjectStorageService implements ObjectStorageService {

    private final AmazonS3 s3;

    public NcpObjectStorageService(NaverConfig naverConfig) {
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        naverConfig.getEndPoint(), naverConfig.getRegionName()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        naverConfig.getAccessKey(), naverConfig.getSecretKey())))
                .build();
    }

    @Override
    public String uploadFile(String bucketName, String directoryPath, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try (InputStream fileIn = file.getInputStream()) {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());

            PutObjectRequest objectRequest = new PutObjectRequest(
                    bucketName,
                    directoryPath + "/" + filename,
                    fileIn,
                    objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);

            s3.putObject(objectRequest);
            return s3.getUrl(bucketName, directoryPath + "/" + filename).toString(); // URL 반환

        } catch (Exception e) {
            throw new RuntimeException("File upload error", e);
        }
    }

    @Override
    public void deleteFile(String bucketName, String directoryPath, String fileName) {
        String path = directoryPath + "/" + fileName;
        if (s3.doesObjectExist(bucketName, path)) {
            s3.deleteObject(bucketName, path);
            System.out.println("File deleted: " + path);
        } else {
            System.out.println("File not found: " + path);
        }
    }
}
