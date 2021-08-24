package net.zoostar.wms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import net.zoostar.wms.model.Case;

public interface OrderService {

	List<ResponseEntity<Case>> order(Case order);

}
