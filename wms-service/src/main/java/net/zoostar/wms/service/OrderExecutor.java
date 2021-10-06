package net.zoostar.wms.service;

import org.springframework.http.HttpHeaders;

import net.zoostar.wms.api.outbound.Order;

public interface OrderExecutor {
	void order(String url,
			HttpHeaders headers, Order order);
}
