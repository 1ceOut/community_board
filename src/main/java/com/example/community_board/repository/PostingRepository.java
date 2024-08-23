package com.example.community_board.repository;

import com.example.community_board.entity.PostingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.PipedInputStream;
import java.util.List;
import java.util.Optional;


public interface PostingRepository extends MongoRepository<PostingEntity, String>{
    //삭제
    void deleteById(String postingId);

    //모든게시물 조회
    List<PostingEntity> findAll();

    //이름으로 조회
    List<PostingEntity> findByTitle(String title);

    //_id로 조회
    List<PostingEntity> findByPostingId(String postingId);


}
