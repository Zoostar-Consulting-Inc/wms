package net.zoostar.wms.service;

import java.util.Map;

import org.springframework.data.domain.Persistable;

public interface Repositories<T extends Persistable<String>> {
	 Map<String, T> getRepository(Class<T> clazz);
}
