package com.erofivan.application.abstractions.persistence.repositories;

import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.users.Client;
import com.erofivan.domain.users.Manager;
import com.erofivan.domain.users.SystemAdministrator;
import com.erofivan.domain.users.WarehouseAdministrator;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void addClient(Client client);

    void addManager(Manager manager);

    void addWarehouseAdministrator(WarehouseAdministrator warehouseAdministrator);

    void addSystemAdministrator(SystemAdministrator systemAdministrator);

    Optional<Client> findClientById(UserId id);

    Optional<Manager> findManagerById(UserId id);

    List<Manager> listManagers();

    List<Client> listClients();
}
