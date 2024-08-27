package com.example.community_board.repository;

import com.example.community_board.entity.PostingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostingRepository extends MongoRepository<PostingEntity, String> {

    // 삭제
    void deleteById(String postingId);

    // 모든 게시물 조회
    List<PostingEntity> findAll();

    // 제목으로 조회
    List<PostingEntity> findByTitle(String title);

    // 게시물 ID로 조회 (Optional 사용)
    Optional<PostingEntity> findByPostingId(String postingId);

    List<PostingEntity> findByUser_id(String userId);
}
