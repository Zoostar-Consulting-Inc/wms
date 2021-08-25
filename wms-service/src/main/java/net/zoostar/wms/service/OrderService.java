package net.zoostar.wms.service;

import org.springframework.http.HttpEntity;

import net.zoostar.wms.model.Order;
import net.zoostar.wms.web.response.OrderSubmitResponse;

public interface OrderService {
	OrderSubmitResponse submit(String url, HttpEntity<Order> request);
}
