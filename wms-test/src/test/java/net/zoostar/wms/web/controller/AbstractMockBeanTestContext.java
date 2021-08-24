package net.zoostar.wms.web.controller;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import net.zoostar.wms.model.AbstractStringPersistable;
import net.zoostar.wms.service.Repositories;

@Getter
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractMockBeanTestContext<T extends AbstractStringPersistable>
		extends AbstractControllerTestContext {
	
	@Autowired
	protected Repositories<T> repositories;
	

}
