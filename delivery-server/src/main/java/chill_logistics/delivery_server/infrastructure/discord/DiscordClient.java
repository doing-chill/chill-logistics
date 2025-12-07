package chill_logistics.delivery_server.infrastructure.discord;

public interface DiscordClient {

    void sendDeadlineMessage(String messageContent);

}
