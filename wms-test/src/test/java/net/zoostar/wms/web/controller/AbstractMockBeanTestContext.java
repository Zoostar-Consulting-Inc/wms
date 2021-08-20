package net.zoostar.wms.web.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.Getter;
import net.zoostar.wms.model.AbstractStringPersistable;
import net.zoostar.wms.web.service.impl.EntityRepository;

@Getter
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractMockBeanTestContext<T extends AbstractStringPersistable>
		extends AbstractControllerTestContext {
	
	@Autowired
	protected EntityRepository<T> repositories;
	
	protected Map<String, T> repository;
	
	@BeforeAll
	void beforeAll() throws JsonParseException, JsonMappingException, IOException {
		var entities = entityMap(getPath(), getClazz());
		log.info("Loaded {} entities of type: {}", entities.size(), getClazz());
		if(log.isDebugEnabled()) {
			int x = 0;
			for(String key : entities.keySet()) {
				log.debug("Entity {}: {}", ++x, entities.get(key));
			}
		} else {
			log.info("{}", "Set logger to debug mode to list all the entities.");
		}
		repositories.getRepositories().put(getClazz(), entities);
		repository = repositories.getRepositories().get(getClazz());
	}

	protected Map<String, T> entityMap(String path, Class<T> clazz) throws
			JsonParseException, JsonMappingException, IOException {
		log.info("Loading entities from: {}...", path);
		JavaType type = mapper.getTypeFactory().
				constructMapType(HashMap.class, String.class, clazz);
		return Collections.unmodifiableMap(mapper.readValue(
				new ClassPathResource(path).getInputStream(),
				type));
	}

	protected abstract String getPath();

	protected abstract Class<T> getClazz();

}
