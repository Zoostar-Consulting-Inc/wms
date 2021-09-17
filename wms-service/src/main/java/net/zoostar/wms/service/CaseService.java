package net.zoostar.wms.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.entity.Client;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.OrderRequest;
import net.zoostar.wms.model.OrderResponse;

public interface CaseService {

	Map<Client, OrderRequest> splitCase(Case order);

	Collection<OrderResponse> order(Case request);

	ResponseEntity<Case> order(OrderRequest order);
	
	HttpHeaders getHeaders();

}
