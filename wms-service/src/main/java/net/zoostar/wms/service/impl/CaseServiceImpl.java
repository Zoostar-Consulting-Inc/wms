package net.zoostar.wms.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.Order;
import net.zoostar.wms.service.CaseService;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.OrderService;
import net.zoostar.wms.web.response.OrderSubmitResponse;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CaseServiceImpl implements CaseService, InitializingBean {

	@Value("${order.update.url:localhost}")
	protected String url;

	@Autowired
	protected ClientService clientManager;
	
	@Autowired
	protected OrderService orderManager;
	
	protected HttpHeaders headers;
	
	@Override
	public List<OrderSubmitResponse> order(Case order) {
        Order splitOrder = splitOrder(order);
		var responses = new ArrayList<OrderSubmitResponse>(splitOrder.getUrls().size());
        HttpEntity<Order> request = new HttpEntity<>(splitOrder, headers);
		for(String url : splitOrder.getUrls().keySet()) {
	        log.info("Placing order: {}...", request.toString());
	        responses.add(orderManager.submit(url, request));
		}
		return responses;
	}

	@Override
	public Order splitOrder(Case order) {
		log.info("{}", "Splitting order per unique client...");
		Order splitOrder = new Order(order);
		var urls = splitOrder.getUrls();
		for(String assetId : order.getAssetIds()) {
			Client client = clientManager.retrieveByAssetId(assetId);
			var assetIds = urls.get(client.getBaseUrl());
			if(assetIds == null) {
				assetIds = new HashSet<>();
				urls.put(client.getBaseUrl(), assetIds);
			}
			assetIds.add(assetId);
		}
		return splitOrder;
	}
	
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
	public HttpHeaders getHeaders() {
		return headers;
	}

}
