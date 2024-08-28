package com.example.community_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories
@EnableFeignClients
@ComponentScan({"storage","com.example.*"})
public class CommunityBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityBoardApplication.class, args);
    }

}
