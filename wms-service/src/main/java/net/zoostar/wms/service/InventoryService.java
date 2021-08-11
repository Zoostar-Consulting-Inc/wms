package net.zoostar.wms.service;

import java.util.Set;

import net.zoostar.wms.model.Inventory;

public interface InventoryService {
	Inventory retrieveByAssetId(String assetId);
	Set<Inventory> search(Set<String> searchTerms);
}
