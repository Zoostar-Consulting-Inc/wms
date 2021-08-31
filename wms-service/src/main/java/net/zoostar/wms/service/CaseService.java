package net.zoostar.wms.service;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpHeaders;

import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Order;
import net.zoostar.wms.web.response.ResponseEntityBean;

public interface CaseService {

	Collection<Order> splitCase(Case order);

	List<ResponseEntityBean<Order>> order(Case order);

	ResponseEntityBean<Order> order(Order order);
	
	HttpHeaders getHeaders();

}
