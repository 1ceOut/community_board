package com.example.community_board.service;

import com.example.community_board.entity.PostingEntity;
import com.example.community_board.repository.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostingService {

    @Autowired
    private PostingRepository postingRepository;
    //추가
    public void insertPosting(PostingEntity postingEntity) {
        postingRepository.save(postingEntity);
    }
    //삭제
    public void deletePosting(String postingId) {
        postingRepository.deleteById(postingId);
    }
    //전체게시물 조회
    public List<PostingEntity> getAllPosting() {
        return postingRepository.findAll();
    }
    //이름으로 조회
    public List<PostingEntity> findByTitle(String title) {
        return postingRepository.findByTitle(title);
    }

    public PostingEntity updatePosting(String postingId, PostingEntity postingEntity) {
        if (postingRepository.existsById(postingId)) {
            postingEntity.setPostingId(postingId);
            return postingRepository.save(postingEntity);
        }
        return null;
    }



}
