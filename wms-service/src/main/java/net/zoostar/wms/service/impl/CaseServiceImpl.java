package net.zoostar.wms.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.Order;
import net.zoostar.wms.service.CaseService;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.InventoryService;
import net.zoostar.wms.service.RestProxyService;
import net.zoostar.wms.service.UserService;
import net.zoostar.wms.web.response.ResponseEntityBean;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CaseServiceImpl implements CaseService, InitializingBean {
	
	@Getter
	protected HttpHeaders headers;

	@Autowired
	protected ClientService clientManager;
	
	@Autowired
	protected RestProxyService<Order, Order> restCaseManager;

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
	public List<ResponseEntityBean<Order>> order(Case order) {
		var splitOrders = splitCase(order);
		var responses = new ArrayList<ResponseEntityBean<Order>>(splitOrders.size());
		for(Order splitOrder: splitOrders) {
			var response = order(splitOrder);
			log.info("Response received: {}", response);
	        responses.add(response);
		}
		return responses;
	}

	@Override
	public Collection<Order> splitCase(Case order) {
		log.info("{}", "Splitting order per unique client...");
		var orders = new HashMap<Client, Order>();
		for(String assetId : order.getAssetIds()) {
			Client client = clientManager.retrieveByAssetId(assetId);
			Order splitOrder = orders.get(client);
			if(splitOrder == null) {
				splitOrder = new Order(client.getBaseUrl(), order);
				orders.put(client, splitOrder);
			}
			splitOrder.getAssetIds().add(assetId);
		}
		return orders.values();
	}

	@Override
	public ResponseEntityBean<Order> order(Order order) {
		log.info("Placing order: {}", order);
		return restCaseManager.exchange(order.getUrl(),
				HttpMethod.POST, headers, order, Order.class);
	}

}
