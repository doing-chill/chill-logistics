package chill_logistics.delivery_server.infrastructure.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {

        return ChatClient.builder(chatModel).build();
    }
}
