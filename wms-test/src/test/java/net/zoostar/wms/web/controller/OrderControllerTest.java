package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collections;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.OrderStatus;
import net.zoostar.wms.model.OrderUpdate;
import net.zoostar.wms.service.OrderService;

class OrderControllerTest extends AbstractControllerTestContext {

	@Autowired
	OrderService orderManager;
	
	@Test
	void testSubmitOrder() throws Exception {
		//GIVEN
		var expected = new Case();
		expected.setCaseId("1");
		expected.setCaseDate(1672462800000l);
		expected.setCustomerUcn("01386585");
		expected.setUserId("user1@email.com");
		var assetIds = new TreeSet<String>();
		Collections.addAll(assetIds, "FE28888", "FE18888");
		expected.setAssetIds(assetIds);

		var client = new Client();
		client.setBaseUrl("localhost:" + expected.getCustomerUcn());
		client.setId("1");
		client.setName("Some Restful Client");
		client.setUcn(expected.getCustomerUcn());
		log.info("Found client for UCN: {}", client.toString());
		
		var entity = new HttpEntity<>(expected, orderManager.getHeaders());
		
		//MOCK
		when(clientRepository.findByUcn(expected.getCustomerUcn())).
				thenReturn(client);
		when(restTemplate.exchange(client.getBaseUrl(), HttpMethod.POST, entity, Case.class)).
				thenReturn(new ResponseEntity<Case>(expected, HttpStatus.OK));

		//WHEN
		var response = orderManager.order(expected);
		var actual = response.getBody();

		//THEN
		var another = actual;
		assertEquals(expected, actual);
		assertEquals(another, actual);
		assertNotEquals(null, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertNotNull(actual.toString());
	}

	@Test
	void testSubmitOrderWithNullId() throws Exception {
		//GIVEN
		var url = "/order/submit";
		
		var expected = new Case();
		expected.setCaseDate(1672462800000l);
		expected.setCustomerUcn("01386585");
		expected.setUserId("user1@email.com");
		var assetIds = new TreeSet<String>();
		Collections.addAll(assetIds, "FE28888", "FE18888");
		expected.setAssetIds(assetIds);

		var client = new Client();
		client.setBaseUrl("localhost:" + expected.getCustomerUcn());
		client.setId("1");
		client.setName("Some Restful Client");
		client.setUcn(expected.getCustomerUcn());
		
		var entity = new HttpEntity<>(expected, orderManager.getHeaders());
		
		//MOCK
		when(clientRepository.findByUcn(expected.getCustomerUcn())).
				thenReturn(client);
		when(restTemplate.exchange(client.getBaseUrl(), HttpMethod.POST, entity, Case.class)).
				thenReturn(new ResponseEntity<Case>(expected, HttpStatus.OK));
		
		//WHEN
		var result = mockMvc.perform(post(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(expected))).
				andReturn();

		//THEN
		assertNotNull(result);
		log.info("Result: {}", result.toString());
		var response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		log.info("Response: {}", response.toString());
		var actual = mapper.readValue(response.getContentAsString(), Case.class);
		var another = actual;
		assertEquals(expected, actual);
		assertEquals(another, actual);
		assertNotEquals(null, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertNotNull(actual.toString());
	}
	
	@Test
	void testOrderUpdate() throws Exception {
		//GIVEN
		String url = "/order/update";
		OrderUpdate expected = new OrderUpdate();
		expected.setCaseId("1");
		expected.setStatus(OrderStatus.O);
		var assetIds = new TreeSet<String>();
		Collections.addAll(assetIds, "FE28888", "FE18888");
		expected.setAssetIds(assetIds);
		
		var entity = new HttpEntity<>(expected, orderManager.getHeaders());
		
		//MOCK
		when(restTemplate.exchange("localhost", HttpMethod.POST, entity, OrderUpdate.class)).
				thenReturn(new ResponseEntity<OrderUpdate>(expected, HttpStatus.OK));
		
		//WHEN
		var result = mockMvc.perform(post(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(expected)).
				accept(MediaType.APPLICATION_JSON)).
				andReturn();
		
		//THEN
		assertNotNull(result);
		var response = result.getResponse();
		var actual = mapper.readValue(response.getContentAsString(), OrderUpdate.class);
		var another = actual;
		assertEquals(expected, actual);
		assertEquals(another, actual);
		assertNotEquals(null, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertNotNull(actual.toString());
		assertEquals(OrderStatus.O, actual.getStatus());
		assertEquals(OrderStatus.O.status(), actual.getStatus().status());
	}

}
