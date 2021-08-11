package net.zoostar.wms.service.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.InventoryRepository;
import net.zoostar.wms.model.Inventory;
import net.zoostar.wms.service.InventoryService;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	protected InventoryRepository inventoryRepository;
	
	@Override
	public Inventory retrieveByAssetId(String assetId) {
		log.info("Search for assetId: {}...", assetId);
		var entity = inventoryRepository.findByAssetId(assetId);
		if(entity.isEmpty()) {
			throw new NoSuchElementException("No Inventory found for assetId: " + assetId);
		}
		return entity.get();
	}

	@Override
	public Set<Inventory> search(Set<String> searchTerms) {
		log.info("Search by: {}", searchTerms.toString());
		Set<Inventory> inventories = new TreeSet<>();
		for(String searchTerm : searchTerms) {
			if(searchTerm.endsWith("*")) {
				inventories.addAll(inventoryRepository.findByAssetIdStartsWith(searchTerm.substring(0, searchTerm.length()-1)));
			} else {
				try {
					inventories.add(retrieveByAssetId(searchTerm));
				} catch(NoSuchElementException e) {
					log.warn(e.getMessage());
				}
			}
		}
		return inventories;
	}

}
