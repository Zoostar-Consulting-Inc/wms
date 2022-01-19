package net.zoostar.wms.entity;

public interface EntityWrapper<T extends AbstractMultiSourceStringPersistable> {
	T toEntity();
	Class<T> getClazz();
}