package com.example.community_board.controller;

import com.example.community_board.entity.PostingEntity;
import com.example.community_board.dto.UserDto;
import com.example.community_board.service.PostingService;
import storage.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8080", "https://api.icebuckwheat.kro.kr"}, allowCredentials = "true")
public class PostingController {

    @Autowired
    private PostingService postingService;
    final NcpObjectStorageService storageService;

    private String buckName = "bitcamp-mkj-128";
    private String folderName = "communitythumbnail";


    //사진만 먼저 업로드
    @PostMapping("/posting/upload")
    public Map<String, String> uploadPhoto(@RequestParam("thumbnail") MultipartFile thumbnail) {
        System.out.println("thumbnail" + thumbnail.getOriginalFilename());
        //스토리지에 업로드후 업로드된 파일명 반환
        String postingphoto = storageService.uploadFile(buckName, folderName, thumbnail);
        Map<String, String> map = new HashMap<>();
        map.put("postingphoto", postingphoto);
        return map;
    }

    @PostMapping("/posting/insert")
    public String insertPosting(@RequestBody PostingEntity postingEntity) {
        postingService.insertPosting(postingEntity);
        return "success";
    }

    @GetMapping("/posting/delete")
    public ResponseEntity<String> deletePosting(@RequestParam("postingId") String postingId) {
        try {
            // 포스팅 엔티티를 조회하여 연결된 사진 파일명을 가져옵니다.
            PostingEntity postingEntity = postingService.findByPostingId(postingId);

            // 포스팅 엔티티가 존재하지 않을 경우
            if (postingEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("포스팅을 찾을 수 없습니다.");
            }

            // 포스팅 엔티티에서 사진 파일명을 가져옵니다.
            String photoName = postingEntity.getThumbnail();  // 필드명이 photoName이라고 가정합니다.

            // 사진 파일명이 존재하면 스토리지에서 파일을 삭제합니다.
            if (photoName != null && !photoName.isEmpty()) {
                storageService.deleteFile(buckName, folderName, photoName);
            }

            // 데이터베이스에서 포스팅을 삭제합니다.
            postingService.deletePosting(postingId);

            return ResponseEntity.ok("포스팅이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            // 예외 발생 시 로그를 찍고 적절한 응답을 반환합니다.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("포스팅 삭제 중 오류가 발생했습니다.");
        }
    }


    @GetMapping("/posting/list")
    public List<PostingEntity> getAllPosting() {
        return postingService.getAllPosting();
    }

    @GetMapping("/posting/listtitle")
    public List<PostingEntity> findByTitle(@RequestParam("title") String title) {
        return postingService.findByTitle(title);
    }

    @PutMapping("/posting/update/{postingId}")
    public PostingEntity updatePosting(@PathVariable String postingId, @RequestBody PostingEntity postingEntity) {
        return postingService.updatePosting(postingId, postingEntity);
    }

    @GetMapping("/posting/detail")
    public PostingEntity findByPostingId(@RequestParam("postingId") String postingId) {
        return postingService.findByPostingId(postingId);
    }

    @GetMapping("/posting/userinfo")
    public UserDto getUserInfo(@RequestParam("user_id") String user_id) {
        return postingService.getUserInfo(user_id); // Feign 클라이언트를 통해 사용자 정보 조회
    }

    @GetMapping("/posting/listWithUser")
    public List<Map<String, Object>> getPostWithUserDetails() {
        return postingService.getPostWithUserDetails(); // 포스팅과 유저 정보를 결합하여 반환
    }
}
