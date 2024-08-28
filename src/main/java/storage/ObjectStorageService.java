package storage;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {
    String uploadFile(String bucketName, String directoryPath, MultipartFile file);
    void deleteFile(String bucketName, String directoryPath, String fileName);
}
