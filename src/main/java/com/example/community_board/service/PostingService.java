package com.example.community_board.service;

import com.example.community_board.client.UserClient;
import com.example.community_board.dto.UpdateDto;
import com.example.community_board.dto.UseKafkaDto;
import com.example.community_board.dto.UserDto;
import com.example.community_board.entity.PostingEntity;
import com.example.community_board.repository.PostingRepository;
import org.apache.kafka.common.protocol.types.Field;
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

    public UpdateDto updatePosting(String postingId, UpdateDto updateDto) {
        Query query = new Query(Criteria.where("_id").is(postingId));
        PostingEntity existingPosting = mongoTemplate.findOne(query, PostingEntity.class);

        if (existingPosting != null) {
            if (updateDto.getTitle() != null) existingPosting.setTitle(updateDto.getTitle());
            if (updateDto.getContents() != null) existingPosting.setContents(updateDto.getContents());
            if (updateDto.getTags() != null) existingPosting.setTags(updateDto.getTags());
            if (updateDto.getWriteday() != null) existingPosting.setWriteday(updateDto.getWriteday());
            if (updateDto.getUserId() != null) existingPosting.setUserId(updateDto.getUserId());
            if (updateDto.getThumbnail() != null) existingPosting.setThumbnail(updateDto.getThumbnail());
            if (updateDto.getSteps() != null) existingPosting.setSteps(updateDto.getSteps());

            mongoTemplate.save(existingPosting);

            return new UpdateDto(
                    existingPosting.getContents(),
                    existingPosting.getTitle(),
                    existingPosting.getTags(),
                    existingPosting.getUserId(),
                    existingPosting.getSteps(),
                    existingPosting.getWriteday(),
                    existingPosting.getThumbnail()
            );
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

    public Map<String, Object> findPostWithUserDetails(String postingId) {
        PostingEntity post = findByPostingId(postingId);
        if (post == null) {
            return null;
        }

        UserDto user = getUserInfo(post.getUserId());
        return Map.of(
                "posting", post,
                "userName", user != null ? user.getName() : "Unknown User",
                "userProfile", user != null ? user.getPhoto() : "/assets/cha.png"
        );
    }


}
