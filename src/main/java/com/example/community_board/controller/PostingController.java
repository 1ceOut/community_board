package com.example.community_board.controller;

import com.example.community_board.entity.PostingEntity;
import com.example.community_board.repository.PostingRepository;
import com.example.community_board.service.PostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")


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

}