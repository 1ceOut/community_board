package com.example.community_board.controller;

import com.example.community_board.dto.UpdateDto;
import com.example.community_board.entity.PostingEntity;
import com.example.community_board.dto.UserDto;
import com.example.community_board.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import storage.NcpObjectStorageService;

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

    private final String buckName = "bitcamp-mkj-128";
    private final String folderName = "communitythumbnail";

    @PostMapping("/posting/upload")
    public Map<String, String> uploadPhoto(@RequestParam("thumbnail") MultipartFile thumbnail) {
        String postingPhoto = storageService.uploadFile(buckName, folderName, thumbnail);
        Map<String, String> map = new HashMap<>();
        map.put("postingphoto", postingPhoto);
        return map;
    }

    @PostMapping("/posting/insert")
    public ResponseEntity<String> insertPosting(@RequestBody PostingEntity postingEntity) {
        postingService.insertPosting(postingEntity);
        return ResponseEntity.ok(postingEntity.getPostingId());
    }

    @DeleteMapping("/posting/delete")
    public ResponseEntity<String> deletePosting(@RequestParam("postingId") String postingId) {
        try {
            PostingEntity postingEntity = postingService.findByPostingId(postingId);

            if (postingEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Posting not found.");
            }

            String photoName = postingEntity.getThumbnail();

            if (photoName != null && !photoName.isEmpty()) {
                storageService.deleteFile(buckName, folderName, photoName);
            }

            postingService.deletePosting(postingId);

            return ResponseEntity.ok("Posting successfully deleted.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting posting.");
        }
    }

    @GetMapping("/posting/list")
    public ResponseEntity<List<PostingEntity>> getAllPosting() {
        List<PostingEntity> postings = postingService.getAllPosting();
        return ResponseEntity.ok(postings);
    }

    @GetMapping("/posting/listtitle")
    public ResponseEntity<List<PostingEntity>> findByTitle(@RequestParam("title") String title) {
        List<PostingEntity> postings = postingService.findByTitle(title);
        return ResponseEntity.ok(postings);
    }

    @PutMapping("/posting/update/{postingId}")
    public ResponseEntity<UpdateDto> updatePosting(@PathVariable String postingId,
                                                   @RequestBody UpdateDto updateDto) {
        System.out.println("PostingId: " + postingId);
        System.out.println("UpdateDto: " + updateDto);

        UpdateDto updatedPosting = postingService.updatePosting(postingId, updateDto);
        if (updatedPosting != null) {
            return ResponseEntity.ok(updatedPosting);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/posting/detail")
    public ResponseEntity<PostingEntity> findByPostingId(@RequestParam("postingId") String postingId) {
        PostingEntity posting = postingService.findByPostingId(postingId);
        if (posting != null) {
            return ResponseEntity.ok(posting);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/posting/userinfo")
    public ResponseEntity<UserDto> getUserInfo(@RequestParam("user_id") String userId) {
        UserDto user = postingService.getUserInfo(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/posting/listWithUser")
    public ResponseEntity<List<Map<String, Object>>> getPostWithUserDetails() {
        List<Map<String, Object>> postsWithUserDetails = postingService.getPostWithUserDetails();
        return ResponseEntity.ok(postsWithUserDetails);
    }

    @GetMapping("/posting/listByUser")
    public ResponseEntity<List<Map<String, Object>>> getPostByUserDetails(@RequestParam("user_id") String userId) {
        List<Map<String, Object>> postsByUserDetails = postingService.getPostByUserDetails(userId);
        return ResponseEntity.ok(postsByUserDetails);
    }

    @GetMapping("/posting/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = postingService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/posting/detailWithUser")
    public ResponseEntity<Map<String, Object>> findPostWithUserDetails(@RequestParam("postingId") String postingId) {
        Map<String, Object> postWithUserDetails = postingService.findPostWithUserDetails(postingId);
        if (postWithUserDetails != null) {
            return ResponseEntity.ok(postWithUserDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
