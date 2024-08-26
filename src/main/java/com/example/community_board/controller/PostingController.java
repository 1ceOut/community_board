package com.example.community_board.controller;

import com.example.community_board.entity.PostingEntity;
import com.example.community_board.repository.PostingRepository;
import com.example.community_board.service.PostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = {"http://localhost:8080","http://reacticeout.icebuckwheat.kro.kr"}) // CORS 설정 (필요에 따라 조정)


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
    public ResponseEntity<List<PostingEntity>> getAllPosting() {
        return ResponseEntity.ok(postingService.getAllPosting());
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
    public List<PostingEntity> findByPostingId(String postingId) {
        return postingService.findByPostingId(postingId);
    }

}