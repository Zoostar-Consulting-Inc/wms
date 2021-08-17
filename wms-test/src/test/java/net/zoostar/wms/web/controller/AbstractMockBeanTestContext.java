package net.zoostar.wms.web.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.zoostar.wms.model.AbstractStringPersistable;

public abstract class AbstractMockBeanTestContext<T extends AbstractStringPersistable>
		extends AbstractControllerTestContext {
	
	protected List<T> entities;

	@BeforeEach
	@Override
	protected void beforeEach(TestInfo test) throws Exception {
		super.beforeEach(test);
		entities = entities(getPath(), getClazz());
		log.info("Loaded {} entities.", entities.size());
	}

	protected List<T> entities(String path, Class<T> clazz) throws
			JsonParseException, JsonMappingException, IOException {
		log.info("Loading {} entities from: {}...", clazz, path);
		JavaType type = mapper.getTypeFactory().
				constructCollectionType(List.class, clazz);
		return Collections.unmodifiableList(mapper.readValue(
				new ClassPathResource(path).getInputStream(),
				type));
	}

	protected abstract String getPath();

	protected abstract Class<T> getClazz();

}
