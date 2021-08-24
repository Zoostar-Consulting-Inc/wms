package net.zoostar.wms.service;

import java.util.Map;

import net.zoostar.wms.model.AbstractStringPersistable;

public interface Repositories<T extends AbstractStringPersistable> {
	 Map<String, T> getRepository(Class<T> clazz);
}
