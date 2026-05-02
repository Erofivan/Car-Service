package com.erofivan.infrastructure.kafka;

import com.erofivan.domain.models.OutboxEventEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventRelay {
    private static final Clock UTC_CLOCK = Clock.systemUTC();

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void relay() {
        List<OutboxEventEntity> events =
            outboxEventRepository.findTop99ByPublishedAtIsNullOrderByCreatedAtAsc();

        for (OutboxEventEntity event : events) {
            try {
                kafkaTemplate.send(
                    event.getTopic(),
                    event.getId().toString(),
                    event.getPayload()
                );
                
                event.setPublishedAt(Instant.now(UTC_CLOCK));
                outboxEventRepository.save(event);
            } catch (Exception e) {
                log.error("Failed to relay outbox event {}: {}", event.getId(), e.getMessage());
            }
        }
    }
}
