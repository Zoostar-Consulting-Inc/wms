package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import net.zoostar.wms.model.Inventory;
import net.zoostar.wms.web.request.InventorySearchRequest;

class InventoryControllerTest extends AbstractMockBeanTestContext<Inventory> {

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

	@Override
	protected Class<Inventory> getClazz() {
		return Inventory.class;
	}

	@Override
	protected String getPath() {
		return "data/inventory.json";
	}

}
