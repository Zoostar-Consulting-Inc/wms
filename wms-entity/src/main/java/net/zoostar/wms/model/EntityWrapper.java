package net.zoostar.wms.model;

public interface EntityWrapper<T extends AbstractStringPersistable> {
	T toEntity();
}