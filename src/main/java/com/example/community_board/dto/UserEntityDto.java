package com.example.community_board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntityDto implements Serializable {
    private String userId;
    private String email;
    private String name;
    private String photo;
    private String nonpreference;
    private String preference;
    private String allergy;
    private String illness;
    private Integer height;
    private Integer weight;
    private String role;
    private Boolean broadcast;

    public UserDto toUserDto() {
        return UserDto.builder()
                .userId(userId)
                .name(name)
                .photo(photo)
                .build();
    }
}