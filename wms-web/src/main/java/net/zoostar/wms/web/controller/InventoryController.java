package net.zoostar.wms.web.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.api.inbound.InventorySearchRequest;
import net.zoostar.wms.entity.Inventory;
import net.zoostar.wms.service.InventoryService;

@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController extends AbstractCommonErrorHandler<Inventory> {

	@Autowired
	protected InventoryService inventoryManager;
	
	@GetMapping(value = "/retrieve/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Inventory> retrieveByAssetId(@PathVariable String assetId) {
		log.info("API triggered: /retrieve/{}", assetId);
		return new ResponseEntity<>(inventoryManager.retrieveByAssetId(assetId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Inventory>> search(@RequestBody InventorySearchRequest request) {
		log.info("{}", "API triggered: /inventory/search");
		return new ResponseEntity<>(inventoryManager.search(request.getSearchTerms()), HttpStatus.OK);
	}
}
