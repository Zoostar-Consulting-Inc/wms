package net.zoostar.wms.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.api.inbound.OrderRequest;
import net.zoostar.wms.api.outbound.Order;
import net.zoostar.wms.api.outbound.OrderResponse;
import net.zoostar.wms.entity.Client;

public interface OrderService {

	Map<Client, Order> splitOrder(OrderRequest order);

	Collection<OrderResponse> order(OrderRequest request);

	ResponseEntity<OrderRequest> order(Order order);
	
	HttpHeaders getHeaders();

}
