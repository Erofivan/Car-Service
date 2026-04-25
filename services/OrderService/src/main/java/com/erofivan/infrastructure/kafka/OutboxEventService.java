package com.erofivan.infrastructure.kafka;

import com.erofivan.domain.models.OutboxEventEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveEvent(String topic, UUID aggregateId, Object event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            OutboxEventEntity outboxEvent = new OutboxEventEntity();

            outboxEvent.setTopic(topic);
            outboxEvent.setAggregateId(aggregateId);
            outboxEvent.setPayload(payload);

            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox event", e);
        }
    }
}
