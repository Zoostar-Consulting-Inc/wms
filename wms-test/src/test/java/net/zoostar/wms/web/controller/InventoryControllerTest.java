package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.InventoryRepository;
import net.zoostar.wms.model.Inventory;
import net.zoostar.wms.web.request.InventorySearchRequest;

@Slf4j
@WebAppConfiguration
@ActiveProfiles({"test"})
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-web.xml"})
class InventoryControllerTest {

	List<Inventory> entities;
	
	protected final ObjectMapper mapper = new ObjectMapper();
	
	@MockBean
	InventoryRepository inventoryRepository;
	
	@Autowired
	WebApplicationContext wac;
	
	MockMvc mockMvc;
	
	@BeforeEach
	protected final void beforeEach(TestInfo test) throws Exception {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());
		
		entities = entities(path());
		log.info("Loaded {} entities.", entities.size());
		
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	void testFindByAssetId() throws Exception {
		//GIVEN
		var entity = entities.stream().findFirst();
		var expected = entity.get();
		String assetId = expected.getAssetId();
		String url = "/inventory/retrieve/" + assetId;

		//MOCK
		when(inventoryRepository.findByAssetId(assetId)).
				thenReturn(entity);
		
		//WHEN
	    var result = mockMvc.perform(get(url).
			accept(MediaType.APPLICATION_JSON_VALUE)).
	    	andReturn();
	    
		//THEN
		var response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		log.info("Response received: {}", response.getContentAsString());
		
		var actual = mapper.readValue(response.getContentAsString(), Inventory.class);
		log.info("Retrieved entity: {}", actual);
		assertEquals(expected, actual);
		assertNotEquals(actual, null);
		assertFalse(actual.isNew());
		assertEquals(expected, actual);
		assertEquals(expected.getClass(), actual.getClass());
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.toString(), actual.toString());

		assertFalse(StringUtils.isBlank(actual.getAssetId()));
		assertEquals(expected.getAssetId(), actual.getAssetId());
		assertEquals(expected.getSku(), actual.getSku());
		assertEquals(expected.getSource(), actual.getSource());
		assertEquals(expected.getQuantity(), actual.getQuantity());
		assertEquals(expected.getHomeUcn(), actual.getHomeUcn());
		assertEquals(expected.getCurrentUcn(), actual.getCurrentUcn());
	}

	@Test
	void testFindByAssetIdNoSuchElementException() throws Exception {
		String url = "/inventory/retrieve/assetId";
		
		//WHEN
	    var result = mockMvc.perform(get(url).
			accept(MediaType.APPLICATION_JSON_VALUE)).
	    	andReturn();
	    
		//THEN
		var response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	void testSearchAssetId() throws Exception {
		//GIVEN
		var expected = entities.stream().findFirst().get();
		String assetId = expected.getAssetId();
		
		int size = 3;
		Set<Inventory> expectedEntities = new HashSet<>(size);
		entities.stream().filter(entity -> entity.getAssetId().startsWith("FE1")).
		forEach(entity -> expectedEntities.add(entity));

		String url = "/inventory/search";
		var request = new InventorySearchRequest();
		request.getSearchTerms().add(assetId);
		request.getSearchTerms().add("FE1*");

		//MOCK
		when(inventoryRepository.findByAssetId(assetId)).
				thenReturn(Optional.of(expected));
		when(inventoryRepository.findByAssetIdStartsWith("FE1")).
				thenReturn(expectedEntities);
		
		//WHEN
	    var response = mockMvc.perform(post(url).
	    		contentType(MediaType.APPLICATION_JSON).
	    		content(mapper.writeValueAsString(request)).
	    		accept(MediaType.APPLICATION_JSON)).
	    		andReturn().getResponse();
	    
		//THEN
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		log.info("Response received: {}", response.getContentAsString());
		
		Set<Inventory> results = mapper.readValue(response.getContentAsString(),
				mapper.getTypeFactory().constructCollectionType(Set.class, Inventory.class));
		assertNotNull(results);
		log.info("Result: {}", results);
		assertEquals(4, results.size());
	}

	@Test
	void testFindByNonExistentAssetId() throws Exception {
		//GIVEN
		String url = "/inventory/search";
		var request = new InventorySearchRequest();
		request.getSearchTerms().add("assetId");
		
		//WHEN
	    var response = mockMvc.perform(post(url).
	    		contentType(MediaType.APPLICATION_JSON_VALUE).
	    		content(mapper.writeValueAsString(request)).
	    		accept(MediaType.APPLICATION_JSON_VALUE)).
	    		andReturn().getResponse();
	    
		//THEN
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
	}

	protected List<Inventory> entities(String path) throws 
	JsonParseException, JsonMappingException, IOException {
		log.info("Loading {} entities from: {}...", Inventory.class, path);
		JavaType type = mapper.getTypeFactory().
				constructCollectionType(List.class, Inventory.class);
		return Collections.unmodifiableList(mapper.readValue(
				new ClassPathResource(path).getInputStream(),
				type));
	}

	protected String path() {
		return "data/inventory.json";
	}

}
