package net.zoostar.wms.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.OrderUpdate;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.OrderService;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService, InitializingBean {

	@Value("${order.update.url:localhost}")
	protected String url;

	@Autowired
	protected ClientService clientManager;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Getter
	protected HttpHeaders headers;
	
	@Override
	public ResponseEntity<Case> order(Case order) {
		if(StringUtils.isBlank(order.getId())) {
			order.setId(UUID.randomUUID().toString());
		}
		
		String ucn = order.getCustomerUcn();
		Client client = clientManager.retrieve(ucn);
        HttpEntity<Case> request = new HttpEntity<>(order, headers);
        log.info("Placing order to {} with Request {}...", client.getBaseUrl(), request.toString());
        return restTemplate.exchange(client.getBaseUrl(), HttpMethod.POST, request, Case.class);
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
	public ResponseEntity<OrderUpdate> update(OrderUpdate update) {
		if(StringUtils.isBlank(update.getId())) {
			update.setId(UUID.randomUUID().toString());
		}
		
        HttpEntity<OrderUpdate> request = new HttpEntity<>(update, headers);
        log.info("Updating order status to {} with Request {}...", url, request.toString());
        return restTemplate.exchange(url, HttpMethod.POST, request, OrderUpdate.class);
	}

}
