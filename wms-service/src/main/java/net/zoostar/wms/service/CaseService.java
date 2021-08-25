package net.zoostar.wms.service;

import java.util.List;

import org.springframework.http.HttpHeaders;

import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Order;
import net.zoostar.wms.web.response.OrderSubmitResponse;

public interface CaseService {

	Order splitOrder(Case order);
	
	List<OrderSubmitResponse> order(Case order);

	HttpHeaders getHeaders();

}
