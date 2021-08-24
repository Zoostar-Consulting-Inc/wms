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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import net.zoostar.wms.model.Customer;
import net.zoostar.wms.service.Repositories;
import net.zoostar.wms.web.request.CustomerSearchRequest;

class CustomerControllerTest extends AbstractControllerTestContext {
	
	@Autowired
	protected Repositories<Customer> repositories;

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
		assertEquals(expected.getSource(), actual.getSource());
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

}
