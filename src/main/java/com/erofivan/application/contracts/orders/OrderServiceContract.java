package com.erofivan.application.contracts.orders;

import com.erofivan.application.contracts.orders.operations.PlaceCustomOrder;
import com.erofivan.application.contracts.orders.operations.PlaceInventoryOrder;

public interface OrderServiceContract {
    PlaceInventoryOrder.Response placeInventoryOrder(PlaceInventoryOrder.Request request);

    PlaceCustomOrder.Response placeCustomOrder(PlaceCustomOrder.Request request);
}
