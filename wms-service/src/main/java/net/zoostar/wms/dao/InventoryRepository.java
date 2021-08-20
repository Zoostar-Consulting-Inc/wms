package net.zoostar.wms.dao;

import java.util.Collection;
import java.util.Optional;

import net.zoostar.wms.model.Inventory;

public interface InventoryRepository extends AbstractWmsRepository<Inventory, String> {
	Optional<Inventory> findByAssetId(String assetId);
	Collection<Inventory> findByAssetIdStartsWith(String assetIdWildcard);
}
