package net.zoostar.wms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.service.OrderService;
import net.zoostar.wms.web.request.CaseOrderSubmitRequest;

@Slf4j
@RestController
public class OrderController {

	@Autowired
	private OrderService orderManager;
	
	@PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Case> order(@RequestBody CaseOrderSubmitRequest request) {
		log.info("Case order request received: {}", request);
		var response = orderManager.order(request.toEntity());
		var order = response.getBody();
		log.info("Case order submitted successfully: {}", order);
		return response;
	}
}
