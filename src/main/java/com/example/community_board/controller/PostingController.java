package com.example.community_board.controller;

import com.example.community_board.entity.PostingEntity;
import com.example.community_board.dto.UserDto;
import com.example.community_board.service.PostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:8080", "https://api.icebuckwheat.kro.kr"}, allowCredentials = "true")
public class PostingController {

    @Autowired
    private PostingService postingService;

    @PostMapping("/posting/insert")
    public String insertPosting(@RequestBody PostingEntity postingEntity) {
        postingService.insertPosting(postingEntity);
        return "success";
    }

    @GetMapping("/posting/delete")
    public void deletePosting(@RequestParam("postingId") String postingId) {
        postingService.deletePosting(postingId);
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
