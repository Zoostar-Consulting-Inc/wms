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
import net.zoostar.wms.service.OrderService;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService, InitializingBean {

	@Value("${case.order.url:localhost}")
	protected String url;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Getter
	protected HttpHeaders headers;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<Case> order(Case order) {
		if(StringUtils.isBlank(order.getId())) {
			order.setId(UUID.randomUUID().toString());
		}
		
        HttpEntity<Case> request = new HttpEntity<>(order, headers);
        log.info("Placing order to {} with Request {}...", url, request.toString());
        return restTemplate.exchange(url, HttpMethod.POST, request, Case.class);
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

}
