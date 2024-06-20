package Vortex.postservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic likeTopic(){
        return TopicBuilder
                .name("like")
                .build();
    }
    @Bean
    public NewTopic commentTopic(){
        return TopicBuilder
                .name("comment")
                .build();
    }
}
