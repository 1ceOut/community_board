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

import java.util.HashMap;
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
    private UserClient userClient;

    @Autowired
    private KafkaService kafkaService;

    private Map<String, UserDto> userCache = new HashMap<>();

    public void insertPosting(PostingEntity postingEntity) {
        postingRepository.save(postingEntity);
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
        return postingRepository.findByPostingId(postingId).orElse(null);
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
            if (postingEntity.getUserId() != null) existingPosting.setUserId(postingEntity.getUserId());
            if (postingEntity.getRecipe_id() != null) existingPosting.setRecipe_id(postingEntity.getRecipe_id());
            if (postingEntity.getThumbnail() != null) existingPosting.setThumbnail(postingEntity.getThumbnail());
            if (postingEntity.getSteps() != null) existingPosting.setSteps(postingEntity.getSteps());

            mongoTemplate.save(existingPosting);
            return existingPosting;
        }
        return null;
    }

    private void loadAllUsers() {
        if (userCache.isEmpty()) {
            List<UserDto> users = userClient.getAllUsers();
            userCache = users.stream()
                    .collect(Collectors.toMap(UserDto::getUserId, user -> user));
        }
    }

    public UserDto getUserInfo(String userId) {
        loadAllUsers();
        return userCache.getOrDefault(userId, null);
    }

    public List<UserDto> getAllUsers() {
        loadAllUsers();
        return userCache.values().stream().collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPostWithUserDetails() {
        List<PostingEntity> postings = postingRepository.findAll();
        loadAllUsers();

        return postings.stream().map(post -> {
            UserDto user = getUserInfo(post.getUserId());
            return Map.of(
                    "posting", post,
                    "userName", user != null ? user.getName() : "Unknown User",
                    "userProfile", user != null ? user.getPhoto() : "/assets/cha.png"
            );
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPostByUserDetails(String userId) {
        UserDto user = getUserInfo(userId);
        List<PostingEntity> postings = postingRepository.findByUserId(userId);

        return postings.stream().map(post -> Map.of(
                "posting", post,
                "userName", user != null ? user.getName() : "Unknown User",
                "userProfile", user != null ? user.getPhoto() : "/assets/cha.png"
        )).collect(Collectors.toList());
    }

    // 새로운 메서드 추가: postingId에 맞는 게시물과 사용자 정보를 가져오는 메서드
    public Map<String, Object> findPostWithUserDetails(String postingId) {
        PostingEntity post = findByPostingId(postingId);
        if (post == null) {
            return null; // 게시물이 없는 경우 null 반환
        }

        UserDto user = getUserInfo(post.getUserId());
        return Map.of(
                "posting", post,
                "userName", user != null ? user.getName() : "Unknown User",
                "userProfile", user != null ? user.getPhoto() : "/assets/cha.png"
        );
    }


}
