package com.example.community_board.client;

import com.example.community_board.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "userClient", url = "https://api.icebuckwheat.kro.kr/api/login/getalluser")
public interface UserClient {

    @GetMapping
    List<UserDto> getAllUsers();
}
