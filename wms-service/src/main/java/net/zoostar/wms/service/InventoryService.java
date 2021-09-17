package net.zoostar.wms.service;

import java.util.Set;

import net.zoostar.wms.entity.Inventory;

public interface InventoryService {
	Inventory retrieveByAssetId(String assetId);
	Set<Inventory> search(Set<String> searchTerms);
}
