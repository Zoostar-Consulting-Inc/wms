package net.zoostar.wms.dao;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.model.Inventory;

public interface InventoryRepository extends PagingAndSortingRepository<Inventory, String> {
	Optional<Inventory> findByAssetId(String assetId);
	Set<Inventory> findByAssetIdStartsWith(String assetIdWildcard);
}
