package net.zoostar.wms.service;

import org.springframework.http.ResponseEntity;

import net.zoostar.wms.entity.Source;

public interface SourceService<T> {

	Source retrieve(String sourceCode);

	ResponseEntity<T> retrieve(String sourceCode, String sourceId, Class<T> clazz);

}
