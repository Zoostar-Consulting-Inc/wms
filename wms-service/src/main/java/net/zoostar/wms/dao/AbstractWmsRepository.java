package net.zoostar.wms.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.model.AbstractStringPersistable;

public interface AbstractWmsRepository<T extends AbstractStringPersistable, ID> extends PagingAndSortingRepository<T, ID> {
	
}