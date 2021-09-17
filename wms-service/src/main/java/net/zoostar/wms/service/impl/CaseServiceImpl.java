package net.zoostar.wms.service.impl;

import java.util.Collection;
import java.util.HashMap;

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
import net.zoostar.wms.model.Client;
import net.zoostar.wms.service.CaseService;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.InventoryService;
import net.zoostar.wms.service.UserService;
import net.zoostar.wms.web.request.Case;
import net.zoostar.wms.web.request.OrderRequest;
import net.zoostar.wms.web.response.CaseResponse;

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
	public CaseResponse order(Case order) {
		var responses = new CaseResponse(order);
		var splitOrders = splitCase(order);
		for(OrderRequest splitOrder: splitOrders) {
			var response = order(splitOrder);
			log.info("Response received: {}", response);
			for(String assetId : splitOrder.getAssetIds()) {
				responses.getResponses().put(assetId, response.getStatusCode());
			}
		}
		return responses;
	}

	@Override
	public Collection<OrderRequest> splitCase(Case order) {
		log.info("{}", "Splitting order per unique client...");
		var orders = new HashMap<Client, OrderRequest>();
		for(String assetId : order.getAssetIds()) {
			Client client = clientManager.retrieveByAssetId(assetId);
			var splitOrder = orders.computeIfAbsent(client, k -> new OrderRequest(client.getBaseUrl(), order));
			orders.put(client, splitOrder);
			splitOrder.getAssetIds().add(assetId);
		}
		return orders.values();
	}

	@Override
	public ResponseEntity<OrderRequest> order(OrderRequest order) {
		log.info("Placing order: {}", order);
		return orderServer.exchange(order.getUrl(),
				HttpMethod.POST, new HttpEntity<>(order, headers), OrderRequest.class);
	}

}
