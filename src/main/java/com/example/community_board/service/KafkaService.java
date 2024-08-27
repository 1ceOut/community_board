package com.example.community_board.service;

import com.example.community_board.dto.UseKafkaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, UseKafkaDto> kafkaTemplate;

    public void kafkaSend(UseKafkaDto useKafkaDto) {
        kafkaTemplate.send("writing_post", useKafkaDto);
    }
}
