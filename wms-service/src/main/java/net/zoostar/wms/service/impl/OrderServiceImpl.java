package net.zoostar.wms.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.SplitOrder;
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
	protected RestTemplate orderServer;
	
	protected HttpHeaders headers;
	
	@Override
	public List<ResponseEntity<Case>> order(Case order) {
		var responses = new ArrayList<ResponseEntity<Case>>();
        HttpEntity<Case> request = new HttpEntity<>(order, headers);
        SplitOrder splitOrder = createSplitOrder(order);
		for(Client client : splitOrder.getClients().keySet()) {
	        log.info("Placing order to {} with Request {}...", client.getBaseUrl(), request.toString());
	        var response = orderServer.exchange(client.getBaseUrl(), HttpMethod.POST, request, Case.class);
	        log.info("Response received: {}", response);
	        responses.add(response);
		}
		return responses;
	}

	protected SplitOrder createSplitOrder(Case order) {
		log.info("{}", "Splitting order per unique client...");
		SplitOrder splitOrder = new SplitOrder(order);
		var clients = splitOrder.getClients();
		for(String assetId : order.getAssetIds()) {
			Client client = clientManager.retrieveByAssetId(assetId);
			var assetIds = clients.get(client);
			if(assetIds == null) {
				assetIds = new HashSet<>();
				clients.put(client, assetIds);
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

}
