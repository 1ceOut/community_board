package com.example.community_board.service;

import com.example.community_board.client.UserClient;
import com.example.community_board.dto.UseKafkaDto;
import com.example.community_board.dto.UserDto;
import com.example.community_board.entity.PostingEntity;
import com.example.community_board.repository.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostingService {

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserClient userClient; // Feign 클라이언트 주입

    //kafka setting
    @Autowired
    private KafkaService kafkaService;

    public void insertPosting(PostingEntity postingEntity) {
        postingRepository.save(postingEntity);
        System.out.println(postingEntity.getPostingId());
        //kafka 메세지 전송
        kafkaService.kafkaSend(UseKafkaDto.builder()
                .posting_id(postingEntity.getPostingId())
                .sender(postingEntity.getUserId())
                .build());
    }

    public void deletePosting(String postingId) {
        postingRepository.deleteById(postingId);
    }

    public List<PostingEntity> getAllPosting() {
        return postingRepository.findAll();
    }

    public PostingEntity findByPostingId(String postingId) {
        return postingRepository.findByPostingId(postingId).orElse(null); // Optional을 처리하여 null 반환
    }

    public List<PostingEntity> findByTitle(String title) {
        return postingRepository.findByTitle(title);
    }

    public PostingEntity updatePosting(String postingId, PostingEntity postingEntity) {
        Query query = new Query(Criteria.where("_id").is(postingId));
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

    public UserDto getUserInfo(String userId) {
        return userClient.getUserInfo(userId); // Feign 클라이언트를 통해 사용자 정보 조회
    }

    // 포스팅과 유저 정보를 결합하여 반환하는 메소드
    public List<Map<String, Object>> getPostWithUserDetails() {
        List<PostingEntity> postings = postingRepository.findAll();
        // 유저 정보 캐시를 위한 Map
        Map<String, UserDto> userCache = postings.stream()
                .map(PostingEntity::getUserId)
                .distinct()
                .collect(Collectors.toMap(userId -> userId, userId -> userClient.getUserInfo(userId)));

        // 포스팅과 유저 정보를 결합
        return postings.stream().map(post -> {
            UserDto user = userCache.get(post.getUserId());
            return Map.of(
                    "posting", post,
                    "userName", user != null ? user.getName() : "Unknown User",
                    "userProfile", user != null ? user.getPhoto() : "/assets/cha.png"
            );
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPostByUserDetails(String user_id) {
        UserDto user = userClient.getUserInfo(user_id);

        List<PostingEntity> postings = postingRepository.findByUserId(user_id);

        return postings.stream().map(post -> Map.of(
                "posting", post,
                "userName", user != null ? user.getName() : "Unknown User",
                "userProfile", user != null ? user.getPhoto() : "/assets/cha.png"
        )).collect(Collectors.toList());
    }
}
