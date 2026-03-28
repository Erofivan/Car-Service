package com.erofivan.application.core;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.users.Manager;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ManagerAssignmentService {
    public UserId assign(IPersistenceContext context) {
        List<Manager> managers = context.users().listManagers();
        int index = ThreadLocalRandom.current().nextInt(managers.size());
        return managers.get(index).getId();
    }
}
