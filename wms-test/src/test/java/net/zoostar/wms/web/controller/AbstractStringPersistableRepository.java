package net.zoostar.wms.web.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.model.AbstractStringPersistable;

@Slf4j
@Getter
public class AbstractStringPersistableRepository implements InitializingBean {
	
	protected final Map<Class<AbstractStringPersistable>, Map<String, ? extends AbstractStringPersistable>> repositories = new HashMap<>();
	
	protected final ObjectMapper mapper = new ObjectMapper();

	@Setter
	private Set<Class<AbstractStringPersistable>> entityTypes;
	
	@Setter
	private String location;

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource dir = new ClassPathResource(location);
		log.info("Loading mock json entity data files from: {}", dir);
		for(Class<AbstractStringPersistable> entityType: entityTypes) {
			var canonicalName = entityType.getCanonicalName();
			JavaType type = mapper.getTypeFactory().
					constructMapType(HashMap.class, String.class, entityType);
			String filename = canonicalName.substring(canonicalName.lastIndexOf(".") + 1).toLowerCase() + ".json";
			log.info("Loading data for {}: {}", entityType, filename);
			repositories.putAll(Collections.unmodifiableMap(mapper.readValue(
					new ClassPathResource(location + "/" + filename).getInputStream(),
					type)));
		}
	}
	
}
