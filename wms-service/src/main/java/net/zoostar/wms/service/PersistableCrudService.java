package net.zoostar.wms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Persistable;

public interface PersistableCrudService<T extends Persistable<ID>, ID> {
	T create(T entity);
	Page<T> retrieve(int page, int size);
	T update(T entity);
	T delete(ID id);
}
