package com.erofivan.domain.users;

import com.erofivan.domain.common.ids.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class WarehouseAdministrator {
    private final UserId id;
    private final String fullName;
}
