package com.erofivan.application.abstractions.persistence.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public final class OrderQuery {
    private final String clientId;
    private final String managerId;
    private final String status;
}
