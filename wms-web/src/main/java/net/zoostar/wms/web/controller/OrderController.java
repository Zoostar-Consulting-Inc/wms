package net.zoostar.wms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.OrderUpdate;
import net.zoostar.wms.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderManager;
	
	@PostMapping(value = "/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Case> order(@RequestBody Case order) {
		log.info("Case order request received: {}", order);
		var response = orderManager.order(order);
		log.info("Case order submitted successfully: {}", response.getBody());
		return response;
	}
	
	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderUpdate> update(@RequestBody OrderUpdate update) {
		log.info("Case order update received: {}", update);
		var response = orderManager.update(update);
		log.info("Case order updated successfully: {}", response.getBody());
		return response;
	}
}
