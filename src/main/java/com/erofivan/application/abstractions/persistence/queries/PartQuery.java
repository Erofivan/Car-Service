package com.erofivan.application.abstractions.persistence.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public final class PartQuery {
    private final String nameLike;
}
