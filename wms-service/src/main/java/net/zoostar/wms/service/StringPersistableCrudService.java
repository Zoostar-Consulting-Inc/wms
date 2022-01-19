package net.zoostar.wms.service;

import org.springframework.data.domain.Page;

import net.zoostar.wms.entity.AbstractMultiSourceStringPersistable;

public interface StringPersistableCrudService<T extends AbstractMultiSourceStringPersistable> {
	T create(T entity);
	Page<T> retrieve(int page, int size);
	T retrieveByKey(T entity);
	T update(T entity);
	T delete(String id);
}
