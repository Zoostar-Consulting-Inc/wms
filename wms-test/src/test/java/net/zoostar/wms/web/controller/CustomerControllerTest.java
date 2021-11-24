package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.api.inbound.CustomerSearchRequest;
import net.zoostar.wms.entity.Customer;
import net.zoostar.wms.entity.Source;
import net.zoostar.wms.service.SourceService;
import net.zoostar.wms.service.TestDataRepositories;

class CustomerControllerTest extends AbstractControllerTestContext<Customer> {
	
	@Autowired
	protected TestDataRepositories<Customer> repositories;
	
	@Autowired
	protected SourceService<Customer> sourceManager;

	@Test
	void testFindByEmail() throws Exception {
		//GIVEN
		var entity = repositories.getRepository(Customer.class).
				entrySet().stream().findFirst();
		var expected = entity.get().getValue();
		String email = expected.getEmail();
		String url = "/customer/retrieve/" + email;

		//MOCK
		when(customerRepository.findByEmail(email)).
				thenReturn(Optional.of(expected));
		
		//WHEN
	    var result = mockMvc.perform(get(url).
			accept(MediaType.APPLICATION_JSON_VALUE)).
	    	andReturn();
	    
		//THEN
		var response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		log.info("Response received: {}", response.getContentAsString());
		
		var actual = mapper.readValue(response.getContentAsString(), Customer.class);
		log.info("Retrieved entity: {}", actual);
		assertEquals(expected, actual);
		assertNotEquals(actual, null);
		assertFalse(actual.isNew());
		assertEquals(expected, actual);
		assertEquals(expected.getClass(), actual.getClass());
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.toString(), actual.toString());

		assertFalse(StringUtils.isBlank(actual.getEmail()));
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getLocationId(), actual.getLocationId());
		assertEquals(expected.getSourceCode(), actual.getSourceCode());
		assertEquals(expected.getSourceId(), actual.getSourceId());
		assertEquals(expected.getName(), actual.getName());
	}

	@Test
	void testFindByEmailNoSuchElementException() throws Exception {
		String url = "/customer/retrieve/email";
		
		//WHEN
	    var result = mockMvc.perform(get(url).
			accept(MediaType.APPLICATION_JSON_VALUE)).
	    	andReturn();
	    
		//THEN
		var response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	void testSearchEmail() throws Exception {
		//GIVEN
		var entities = repositories.getRepository(Customer.class).entrySet();
		var expected = entities.stream().findFirst().get();
		String email = expected.getValue().getEmail();
		
		int size = 2;
		Set<Customer> expectedEntities = new HashSet<>(size);
		entities.stream().filter(entity -> entity.getValue().getEmail().startsWith("user1")).
				forEach(entity -> expectedEntities.add(entity.getValue()));

		String url = "/customer/search";
		var request = new CustomerSearchRequest();
		request.getSearchTerms().add(email);
		request.getSearchTerms().add("user1*");

		//MOCK
		when(customerRepository.findByEmail(email)).
				thenReturn(Optional.of(expected.getValue()));
		when(customerRepository.findByEmailStartsWith("user1")).
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
		
		Set<Customer> results = mapper.readValue(response.getContentAsString(),
				mapper.getTypeFactory().constructCollectionType(Set.class, Customer.class));
		assertNotNull(results);
		log.info("Result: {}", results);
		assertEquals(3, results.size());
	}

	@Test
	void testFindByNonExistentEmail() throws Exception {
		//GIVEN
		String url = "/customer/search";
		var request = new CustomerSearchRequest();
		request.getSearchTerms().add("email");
		
		//WHEN
	    var response = mockMvc.perform(post(url).
	    		contentType(MediaType.APPLICATION_JSON_VALUE).
	    		content(mapper.writeValueAsString(request)).
	    		accept(MediaType.APPLICATION_JSON_VALUE)).
	    		andReturn().getResponse();
	    
		//THEN
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
	}

	@Test
	void testCreate() throws Exception {
		//given
		String sourceCode = "sph";
		String url = "/customer/update/" + sourceCode;
		String sourceId = UUID.randomUUID().toString();
		var source = new Source();
		source.setBaseUrl("url");
		source.setSourceCode(sourceCode);
		source.setId(UUID.randomUUID().toString());
		
		//mock-when
		var expected = repositories.getRepository(Customer.class).
				entrySet().stream().findFirst().get().getValue();
		var entity = new Customer();
		entity.setEmail("peter2@spuh.org");
		entity.setSourceCode(sourceCode);
		entity.setLocationId("01386586");
		entity.setName("St. Peters2 Univ Hosp");
		entity.setSourceId(sourceId);

		when(sourceRepository.findBySourceCode(sourceCode)).
			thenReturn(Optional.of(source));
		
		when(sourceManager.retrieve(sourceCode, sourceId, Customer.class)).
			thenReturn(new ResponseEntity<>(entity, HttpStatus.OK));
		
		when(customerRepository.save(entity)).
			thenReturn(expected);
		
		var response = mockMvc.perform(post(url).
				param("sourceId", sourceId).
				accept(MediaType.APPLICATION_JSON_VALUE)).
				andReturn().getResponse();
		
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		Customer actual = mapper.readValue(response.getContentAsString(), Customer.class);
		assertEquals(expected, actual);
		assertNotEquals(actual, null);
		assertFalse(actual.isNew());
		assertEquals(expected, actual);
		assertEquals(expected.getClass(), actual.getClass());
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.toString(), actual.toString());

		assertFalse(StringUtils.isBlank(actual.getEmail()));
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getLocationId(), actual.getLocationId());
		assertEquals(expected.getSourceCode(), actual.getSourceCode());
		assertEquals(expected.getSourceId(), actual.getSourceId());
		assertEquals(expected.getName(), actual.getName());
		
		assertTrue(source.hashCode() > 0);
		assertEquals(source, new Source("sph", "clone"));
		var clone = source;
		assertEquals(source, clone);
	}

	@Test
	void testUpdate() throws Exception {
		var expected = repositories.getRepository(Customer.class).
				entrySet().stream().findFirst().get().getValue();

		//given
		String sourceCode = expected.getSourceCode();
		String sourceId = expected.getSourceId();
		String url = "/customer/update/" + sourceCode;
		
		var source = new Source();
		source.setBaseUrl("url");
		source.setSourceCode(sourceCode);
		source.setId(UUID.randomUUID().toString());
		
		//mock-when
		var entity = new Customer();
		entity.setEmail("peter2@spuh.org");
		entity.setSourceCode(sourceCode);
		entity.setSourceId(sourceId);
		entity.setLocationId(expected.getLocationId());
		entity.setName(expected.getName());

		when(sourceRepository.findBySourceCode(sourceCode)).
			thenReturn(Optional.of(source));
		
		when(sourceManager.retrieve(sourceCode, sourceId, Customer.class)).
			thenReturn(new ResponseEntity<>(entity, HttpStatus.OK));
		
		when(customerRepository.findBySourceCodeAndSourceId(sourceCode, sourceId)).
			thenReturn(Optional.of(expected));
		
		when(customerRepository.save(entity)).
			thenReturn(entity);
		
		var response = mockMvc.perform(post(url).
				param("sourceId", sourceId).
				accept(MediaType.APPLICATION_JSON_VALUE)).
				andReturn().getResponse();
		
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		Customer actual = mapper.readValue(response.getContentAsString(), Customer.class);
		assertEquals(entity, actual);
		assertNotEquals(actual, null);
		assertFalse(actual.isNew());
		assertEquals(entity, actual);
		assertEquals(entity.getClass(), actual.getClass());
		assertEquals(entity.hashCode(), actual.hashCode());
		assertEquals(entity.getId(), actual.getId());
		assertEquals(entity.toString(), actual.toString());

		assertFalse(StringUtils.isBlank(actual.getEmail()));
		assertEquals(entity.getEmail(), actual.getEmail());
		assertEquals(entity.getLocationId(), actual.getLocationId());
		assertEquals(entity.getSourceCode(), actual.getSourceCode());
		assertEquals(entity.getSourceId(), actual.getSourceId());
		assertEquals(entity.getName(), actual.getName());
	}


	@Test
	void testDelete() throws Exception {
		var expected = repositories.getRepository(Customer.class).
				entrySet().stream().findFirst().get().getValue();

		//given
		String sourceCode = expected.getSourceCode();
		String sourceId = expected.getSourceId();
		String url = "/customer/update/" + sourceCode;
		
		//mock-when
		var source = new Source();
		source.setBaseUrl("url");
		source.setSourceCode(sourceCode);
		source.setId(UUID.randomUUID().toString());
		
		when(sourceRepository.findBySourceCode(sourceCode)).
			thenReturn(Optional.of(source));
		
		when(sourceManager.retrieve(sourceCode, sourceId, Customer.class)).
			thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
		
		when(customerRepository.findBySourceCodeAndSourceId(sourceCode, sourceId)).
			thenReturn(Optional.of(expected));
		
		when(customerRepository.findById(expected.getId())).
			thenReturn(Optional.of(expected));

		var response = mockMvc.perform(post(url).
				param("sourceId", sourceId).
				accept(MediaType.APPLICATION_JSON_VALUE)).
				andReturn().getResponse();
		
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		Customer actual = mapper.readValue(response.getContentAsString(), Customer.class);
		assertEquals(expected, actual);
		assertNotEquals(actual, null);
		assertFalse(actual.isNew());
		assertEquals(expected, actual);
		assertEquals(expected.getClass(), actual.getClass());
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.toString(), actual.toString());

		assertFalse(StringUtils.isBlank(actual.getEmail()));
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getLocationId(), actual.getLocationId());
		assertEquals(expected.getSourceCode(), actual.getSourceCode());
		assertEquals(expected.getSourceId(), actual.getSourceId());
		assertEquals(expected.getName(), actual.getName());
	}
}
