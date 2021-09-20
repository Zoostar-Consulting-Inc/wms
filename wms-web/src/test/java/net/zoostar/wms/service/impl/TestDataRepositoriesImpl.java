package net.zoostar.wms.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.service.TestDataRepositories;

@Slf4j
public class TestDataRepositoriesImpl<T> implements TestDataRepositories<T>, InitializingBean {

	@Value("${repositories.path:data}")
	private String path;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Setter
	private Set<Class<T>> entityTypes = new HashSet<>();
	
	private final Map<Class<T>, Map<String, T>> repositories = new HashMap<>();
	
	@Override
	public  Map<String, T> getRepository(Class<T> clazz) {
		Map<String, T> repository = repositories.get(clazz);
		log.info("Found {} records in repository: {}", repository.size(), clazz);
		return repository;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadRepositoryData();
	}

	private void loadRepositoryData() throws IOException {
		var dir = new ClassPathResource(path);
		log.info("Loading mock json entity data files from: {}", dir);
		for(Class<T> entityType: entityTypes) {
			var canonicalName = entityType.getCanonicalName();
			var filename = canonicalName.substring(canonicalName.lastIndexOf(".") + 1).toLowerCase() + ".json";
			log.info("Loading data for {}: {}", entityType, filename);
			var type = mapper.getTypeFactory().
					constructMapType(LinkedHashMap.class, String.class, entityType);
			Map<String, T> repository = Collections.unmodifiableMap(mapper.readValue(
					new ClassPathResource(path + "/" + filename).getInputStream(),
					type));
			repositories.put(entityType, repository);
		}
	}
}
