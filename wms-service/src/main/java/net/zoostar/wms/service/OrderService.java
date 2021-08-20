package net.zoostar.wms.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.OrderUpdate;

public interface OrderService {

	HttpHeaders getHeaders();
	
	List<ResponseEntity<Case>> order(Case order);

	ResponseEntity<OrderUpdate> update(OrderUpdate update);

}
