package chill_logistics.delivery_server.infrastructure.ai;

import chill_logistics.delivery_server.infrastructure.ai.dto.request.AiDeadlineRequestV1;
import chill_logistics.delivery_server.infrastructure.ai.dto.response.AiDeadlineResponseV1;

public interface AiClient {

    AiDeadlineResponseV1 generateDeadlineMessage(AiDeadlineRequestV1 request);
}
