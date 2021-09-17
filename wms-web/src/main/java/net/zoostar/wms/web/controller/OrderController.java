package net.zoostar.wms.web.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.OrderResponse;
import net.zoostar.wms.service.CaseService;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private CaseService orderManager;
	
	@PostMapping(value = "/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<OrderResponse>> orderSubmit(@RequestBody Case request) {
		log.info("Case order request received: {}", request);
		return new ResponseEntity<>(orderManager.order(request), HttpStatus.OK);
	}

}
