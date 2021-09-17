package net.zoostar.wms.dao;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.entity.Inventory;

public interface InventoryRepository extends PagingAndSortingRepository<Inventory, String> {
	Optional<Inventory> findByAssetId(String assetId);
	Collection<Inventory> findByAssetIdStartsWith(String assetIdWildcard);
}
