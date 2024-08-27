package com.example.community_board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Document(collection = "posting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostingEntity {

    @Id
    private String postingId;
    @Field
    private String title; // 레시피 이름
    @Field
    private String contents; // 레시피 내용
    @Field
    private String tags; //태그
    @Field
    private String used; //쉼표로 분리해서 사용?
    @Field
    private float diff; //0.5단위로 별표 표기 (별점같음)
    @Field
    private int time; // 시간
    @Field
    private byte amount; // 인원수단위
    @Field
    private LocalDate writeday; // 등록일자
    @Field
    private int likes; // 좋아요..?
    @Field
    private int views; //조회수
    @Field
    private float grade; // 댓글에 달린 평점 기반 산정 결과
    @Field("user_id")
    private String userId; // 유저아이디
    @Field
    private String recipe_id; // 레시피아이디
    @Field
    private String thumbnail; // 썸네일
    @Field
    private List<Map<String, String>> steps; // 레시피 단계들
}
