package net.zoostar.wms.service;

import net.zoostar.wms.entity.Customer;
import net.zoostar.wms.entity.Source;

public interface SourceService<T> {

	Source retrieve(String sourceCode);

	Customer retrieve(String sourceCode, String sourceId, Class<T> clazz);

}
