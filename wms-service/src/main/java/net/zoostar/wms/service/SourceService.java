package net.zoostar.wms.service;

import net.zoostar.wms.entity.Source;

public interface SourceService<T> {

	Source retrieve(String sourceCode);

	T retrieve(String sourceCode, String sourceId, Class<T> clazz);

}
