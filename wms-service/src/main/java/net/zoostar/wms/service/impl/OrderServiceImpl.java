package net.zoostar.wms.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.api.inbound.OrderRequest;
import net.zoostar.wms.api.outbound.Order;
import net.zoostar.wms.api.outbound.OrderResponse;
import net.zoostar.wms.entity.Client;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.InventoryService;
import net.zoostar.wms.service.OrderService;
import net.zoostar.wms.service.UserService;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService, InitializingBean {
	
	@Getter
	protected HttpHeaders headers;

	@Autowired
	protected ClientService clientManager;
	
	@Autowired
	protected RestTemplate orderServer;

	@Autowired
	protected InventoryService inventoryManager;
	
	@Autowired
	protected UserService userManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initHttpHeaders();
	}
	
	protected void initHttpHeaders() {
		log.info("{}", "Initializing HttpHeaders...");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
        log.info("HttpHeaders initialized: {}.", headers);
	}
	
	@Override
	public Collection<OrderResponse> order(OrderRequest order) {
		var splitOrders = splitOrder(order);
		var responses = new HashSet<OrderResponse>(splitOrders.size());
		for(Entry<Client, Order> entry : splitOrders.entrySet()) {
			var response = order(entry.getValue());
			log.info("Response received: {}", response);
			responses.add(new OrderResponse(response, entry.getKey().getCode()));
		}
		return responses;
	}

	@Override
	public Map<Client, Order> splitOrder(OrderRequest order) {
		log.info("{}", "Splitting order per unique client...");
		var orders = new HashMap<Client, Order>();
		for(String assetId : order.getAssetIds()) {
			Client client = clientManager.retrieveByAssetId(assetId);
			var splitOrder = orders.computeIfAbsent(client, k -> new Order(client, order));
			orders.put(client, splitOrder);
			splitOrder.getAssetIds().add(assetId);
		}
		return orders;
	}

	@Override
	public ResponseEntity<OrderRequest> order(Order order) {
		log.info("Placing order: {}", order);
		OrderRequest request = order;
		String url = order.getClient().getBaseUrl();
		return orderServer.exchange(url, HttpMethod.POST,
				new HttpEntity<>(request, headers), OrderRequest.class);
	}

}
