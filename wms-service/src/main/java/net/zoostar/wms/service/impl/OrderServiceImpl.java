package net.zoostar.wms.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.api.inbound.OrderRequest;
import net.zoostar.wms.api.inbound.OrderUpdateRequest;
import net.zoostar.wms.api.outbound.Order;
import net.zoostar.wms.entity.Client;
import net.zoostar.wms.entity.Inventory;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.InventoryService;
import net.zoostar.wms.service.OrderExecutor;
import net.zoostar.wms.service.OrderService;
import net.zoostar.wms.service.UserService;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService, InitializingBean, ApplicationContextAware {
	
	@Getter
	protected HttpHeaders headers;

	@Autowired
	protected ClientService clientManager;

	@Autowired
	protected InventoryService inventoryManager;
	
	@Autowired
	protected UserService userManager;

	@Autowired
	protected RestTemplate orderHost;

	@Value("${order.update.server.url")
	protected String orderUpdateServerUrl;
	
	protected ApplicationContext context;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(orderUpdateServerUrl, "Order Update Server URL may not be empty!");
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
	public ResponseEntity<OrderRequest> order(OrderRequest order) {
		var splitOrders = splitOrder(order);
		for(Entry<Client, Order> entry : splitOrders.entrySet()) {
			var client = entry.getKey();
			var orderExecutor = context.getBean(OrderExecutor.class);
			orderExecutor.order(client.getBaseUrl(), headers, entry.getValue());
		}
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@Override
	public Map<Client, Order> splitOrder(OrderRequest order) {
		log.info("{}", "Splitting order per unique client...");
		var orders = new HashMap<Client, Order>();
		for(String assetId : order.getAssetIds()) {
			Inventory inventory = inventoryManager.retrieveByAssetId(assetId);
			Client client = clientManager.retrieveByUcn(inventory.getHomeUcn());
			var splitOrder = orders.computeIfAbsent(client, k -> new Order(client, order));
			orders.put(client, splitOrder);
			splitOrder.getAssetIds().add(assetId);
		}
		return orders;
	}

	@Override
	public ResponseEntity<OrderUpdateRequest> update(OrderUpdateRequest request) {
		log.info("Updating order status: {}", request);
		return orderHost.exchange(orderUpdateServerUrl, HttpMethod.POST,
				new HttpEntity<>(request, headers), OrderUpdateRequest.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}
