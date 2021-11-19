package net.zoostar.wms.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import net.zoostar.wms.api.inbound.OrderRequest;
import net.zoostar.wms.api.inbound.OrderUpdateRequest;
import net.zoostar.wms.api.outbound.Order;
import net.zoostar.wms.entity.Client;

public interface OrderService {

	Map<Client, Order> splitOrder(OrderRequest order);

	ResponseEntity<OrderRequest> order(OrderRequest request);

	ResponseEntity<OrderUpdateRequest> update(OrderUpdateRequest request);

}
