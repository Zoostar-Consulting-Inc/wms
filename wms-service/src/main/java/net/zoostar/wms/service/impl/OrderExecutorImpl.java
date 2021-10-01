package net.zoostar.wms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.api.inbound.OrderRequest;
import net.zoostar.wms.api.inbound.OrderUpdateRequest;
import net.zoostar.wms.api.outbound.Order;
import net.zoostar.wms.service.OrderExecutor;
import net.zoostar.wms.service.OrderService;

@Slf4j
@Service
public class OrderExecutorImpl implements OrderExecutor {

	@Autowired
	protected RestTemplate orderServer;
	
	@Autowired
	protected OrderService orderManager;
	
	@Async
	public void order(String url,
			HttpHeaders headers, Order order) {
		log.info("Placing order request: {}", order);
		OrderRequest request = order;
			var response = orderServer.exchange(url, HttpMethod.POST,
				new HttpEntity<>(request, headers), OrderRequest.class);
		log.info("Response received: {}", response);
		if(response.getStatusCode() != HttpStatus.OK) {
			var updateRequest = new OrderUpdateRequest();
			updateRequest.setCaseId(url);
			updateRequest.setStatus("F");
			updateRequest.setUcn(order.getClientUcn());
			updateRequest.setAssetIds(order.getAssetIds());
			log.info("Updating order failure: {}", updateRequest);
			var updateResponse = orderManager.update(updateRequest);
			log.info("Failure update response: {}", updateResponse);
		}
	}

}
