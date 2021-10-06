package net.zoostar.wms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.api.inbound.OrderRequest;
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
	}

}
