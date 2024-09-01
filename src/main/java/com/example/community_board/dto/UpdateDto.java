package com.example.community_board.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDto {
    private String contents;
    private String title;
    private String tags;
    private String userId;
    private List<Map<String, String>> steps;
    private LocalDateTime writeday;
    private String thumbnail;
//    private String postingId;
}
