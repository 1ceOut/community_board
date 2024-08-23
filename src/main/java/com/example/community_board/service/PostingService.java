package com.example.community_board.service;

import com.example.community_board.entity.PostingEntity;
import com.example.community_board.repository.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    //_id로 조회
    public List<PostingEntity> findByPostingId(String postingId) {
        return postingRepository.findByPostingId(postingId);
    }
    //이름으로 조회
    public List<PostingEntity> findByTitle(String title) {
        return postingRepository.findByTitle(title);
    }

    @Autowired
    private MongoTemplate mongoTemplate;
    //수정
    public PostingEntity updatePosting(String postingId, PostingEntity postingEntity) {
        Query query = new Query(Criteria.where("postingId").is(postingId));
        PostingEntity existingPosting = mongoTemplate.findOne(query, PostingEntity.class);

        if (existingPosting != null) {
            if (postingEntity.getTitle() != null) existingPosting.setTitle(postingEntity.getTitle());
            if (postingEntity.getContents() != null) existingPosting.setContents(postingEntity.getContents());
            if (postingEntity.getTags() != null) existingPosting.setTags(postingEntity.getTags());
            if (postingEntity.getUsed() != null) existingPosting.setUsed(postingEntity.getUsed());
            if (postingEntity.getDiff() != 0) existingPosting.setDiff(postingEntity.getDiff());
            if (postingEntity.getTime() != 0) existingPosting.setTime(postingEntity.getTime());
            if (postingEntity.getAmount() != 0) existingPosting.setAmount(postingEntity.getAmount());
            if (postingEntity.getWriteday() != null) existingPosting.setWriteday(postingEntity.getWriteday());
            if (postingEntity.getLikes() != 0) existingPosting.setLikes(postingEntity.getLikes());
            if (postingEntity.getViews() != 0) existingPosting.setViews(postingEntity.getViews());
            if (postingEntity.getGrade() != 0) existingPosting.setGrade(postingEntity.getGrade());

            mongoTemplate.save(existingPosting);
            return existingPosting;
        }

        return null;
    }

}
