package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.repositories.UserRepository;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.users.Client;
import com.erofivan.domain.users.Manager;
import com.erofivan.domain.users.SystemAdministrator;
import com.erofivan.domain.users.WarehouseAdministrator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryUserRepository implements UserRepository {
    private final Map<UserId, Client> clients = new LinkedHashMap<>();
    private final Map<UserId, Manager> managers = new LinkedHashMap<>();
    private final Map<UserId, WarehouseAdministrator> warehouseAdmins = new LinkedHashMap<>();
    private final Map<UserId, SystemAdministrator> systemAdmins = new LinkedHashMap<>();

    @Override
    public void addClient(Client client) {
        clients.put(client.getId(), client);
    }

    @Override
    public void addManager(Manager manager) {
        managers.put(manager.getId(), manager);
    }

    @Override
    public void addWarehouseAdministrator(WarehouseAdministrator warehouseAdministrator) {
        warehouseAdmins.put(warehouseAdministrator.getId(), warehouseAdministrator);
    }

    @Override
    public void addSystemAdministrator(SystemAdministrator systemAdministrator) {
        systemAdmins.put(systemAdministrator.getId(), systemAdministrator);
    }

    @Override
    public Optional<Client> findClientById(UserId id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public Optional<Manager> findManagerById(UserId id) {
        return Optional.ofNullable(managers.get(id));
    }

    @Override
    public List<Manager> listManagers() {
        return new ArrayList<>(managers.values());
    }

    @Override
    public List<Client> listClients() {
        return new ArrayList<>(clients.values());
    }
}
