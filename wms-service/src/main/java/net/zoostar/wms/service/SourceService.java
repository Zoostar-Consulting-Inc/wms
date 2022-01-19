package net.zoostar.wms.service;

import org.springframework.http.ResponseEntity;

import net.zoostar.wms.entity.AbstractMultiSourceStringPersistable;
import net.zoostar.wms.entity.EntityWrapper;
import net.zoostar.wms.entity.Source;

public interface SourceService<E extends EntityWrapper<T>, T extends AbstractMultiSourceStringPersistable> {

	Source retrieve(String sourceCode);

	ResponseEntity<E> retrieve(String sourceCode,
			String sourceId, Class<E> clazz);

	E create(String sourceCode, String sourceId, Class<E> clazz);
}
