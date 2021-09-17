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
import net.zoostar.wms.entity.Client;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.OrderRequest;
import net.zoostar.wms.model.OrderResponse;
import net.zoostar.wms.service.CaseService;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.InventoryService;
import net.zoostar.wms.service.UserService;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CaseServiceImpl implements CaseService, InitializingBean {
	
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
	public Collection<OrderResponse> order(Case order) {
		var splitOrders = splitCase(order);
		var responses = new HashSet<OrderResponse>(splitOrders.size());
		for(Entry<Client,OrderRequest> entry : splitOrders.entrySet()) {
			var response = order(entry.getValue());
			log.info("Response received: {}", response);
			responses.add(new OrderResponse(response, entry.getKey().getCode()));
		}
		return responses;
	}

	@Override
	public Map<Client, OrderRequest> splitCase(Case order) {
		log.info("{}", "Splitting order per unique client...");
		var orders = new HashMap<Client, OrderRequest>();
		for(String assetId : order.getAssetIds()) {
			Client client = clientManager.retrieveByAssetId(assetId);
			var splitOrder = orders.computeIfAbsent(client, k -> new OrderRequest(client.getBaseUrl(), order));
			orders.put(client, splitOrder);
			splitOrder.getAssetIds().add(assetId);
		}
		return orders;
	}

	@Override
	public ResponseEntity<Case> order(OrderRequest order) {
		log.info("Placing order: {}", order);
		Case request = order;
		return orderServer.exchange(order.getUrl(),
				HttpMethod.POST, new HttpEntity<>(request, headers), Case.class);
	}

}
