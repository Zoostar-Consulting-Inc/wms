package net.zoostar.wms.service;

import java.util.Map;

public interface TestDataRepositories<T> {
	 Map<String, T> getRepository(Class<T> clazz);
}
