package net.zoostar.wms.web.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.zoostar.wms.dao.CustomerRepository;
import net.zoostar.wms.dao.InventoryRepository;
import net.zoostar.wms.model.AbstractMultiSourceStringPersistable;

@ActiveProfiles({"test"})
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-web.xml"})
public abstract class AbstractMockBeanTestContext<T extends AbstractMultiSourceStringPersistable>
		extends AbstractControllerTestContext {
	
	protected final ObjectMapper mapper = new ObjectMapper();
	
	protected List<T> entities;
	
	@MockBean
	protected CustomerRepository customerRepository;
	
	@MockBean
	protected InventoryRepository inventoryRepository;

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
