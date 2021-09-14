package net.zoostar.wms.service;

import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.web.request.Case;
import net.zoostar.wms.web.request.OrderRequest;
import net.zoostar.wms.web.response.CaseResponse;

public interface CaseService {

	Collection<OrderRequest> splitCase(Case order);

	CaseResponse order(Case request);

	ResponseEntity<OrderRequest> order(OrderRequest order);
	
	HttpHeaders getHeaders();

}
