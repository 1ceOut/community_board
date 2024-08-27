package com.example.community_board.client;

import com.example.community_board.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userClient", url = "https://api.icebuckwheat.kro.kr/api/login")
public interface UserClient {

    @GetMapping("/getuser")
    UserDto getUserInfo(@RequestParam("user_id") String user_id);
}
