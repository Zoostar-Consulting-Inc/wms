package net.zoostar.wms.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import net.zoostar.wms.model.AbstractStringPersistable;

@Getter
@Component
public class EntityRepository<T extends AbstractStringPersistable> {

	private final Map<Class<T>, Map<String, T>> repositories = new HashMap<>();
	
}
