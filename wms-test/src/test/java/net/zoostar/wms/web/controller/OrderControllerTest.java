package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.JavaType;

import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.ClientDetail;
import net.zoostar.wms.model.Customer;
import net.zoostar.wms.model.Inventory;
import net.zoostar.wms.model.User;
import net.zoostar.wms.service.CaseService;
import net.zoostar.wms.service.Repositories;
import net.zoostar.wms.web.response.OrderSubmitResponse;

class OrderControllerTest extends AbstractControllerTestContext {

	@Autowired
	private Repositories<Inventory> inventories;

	@Autowired
	private Repositories<Customer> customers;

	@Autowired
	private Repositories<User> users;
	
	@Autowired
	private Repositories<Client> clients;
	
	@Autowired
	private CaseService caseManager;
	
	@Test
	void testOrderSubmitSuccess() throws Exception {
		//GIVEN
		var url = "/order/submit";
		var inventory = inventories.getRepository(Inventory.class).entrySet().stream().findFirst().get().getValue();
		var customer = customers.getRepository(Customer.class).entrySet().stream().findFirst().get().getValue();
		var user = users.getRepository(User.class).entrySet().stream().findFirst().get().getValue();
		var inboundRequest = caseRequest(inventory, customer, user);
		
		//MOCK
		var client = clients.getRepository(Client.class).entrySet().stream().findFirst().get().getValue();
		log.info("Retrieved mocking client: {}", client);
		var clientDetails = client.getDetails();
		for(ClientDetail detail : clientDetails) {
			log.info("Setting mock client detail with client: {}", detail);
			detail.setClient(client);
		}
		
		when(inventoryRepository.findByAssetId(inventory.getAssetId())).
				thenReturn(Optional.of(inventory));
		
		when(clientDetailsRepository.findByUcn(inventory.getHomeUcn())).
				thenReturn(clientDetails.stream().findFirst());

		var order = caseManager.splitOrder(inboundRequest);
		var outboundRequest = new HttpEntity<>(order, caseManager.getHeaders());
		for(String clientUrl : order.getUrls().keySet()) {
			when(orderManager.submit(clientUrl, outboundRequest)).
					thenReturn(new OrderSubmitResponse(order, HttpStatus.OK));
		}
		
		//WHEN
	    var result = mockMvc.perform(post(url).
	    		contentType(MediaType.APPLICATION_JSON_VALUE).
	    		content(mapper.writeValueAsString(inboundRequest)).
	    		accept(MediaType.APPLICATION_JSON_VALUE)).
	    		andReturn();
	    
	    //THEN
	    assertNotEquals(order, null);
	    assertNotNull(result);
	    var response = result.getResponse();
	    assertNotNull(response);
	    assertEquals(HttpStatus.OK.value(), response.getStatus());
		JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, OrderSubmitResponse.class);
		List<OrderSubmitResponse> actual = mapper.readValue(response.getContentAsString(), javaType);
		assertEquals(1, actual.size());
		for(OrderSubmitResponse entity : actual) {
			log.info("Order Submit Response: {}", entity);
			assertNotEquals(entity, null);
			assertEquals(HttpStatus.OK, entity.getStatus());
			assertEquals(order, entity.getOrder());
			assertEquals(order.hashCode(), entity.getOrder().hashCode());
		}
		
		assertNotEquals(client, null);
		assertFalse(client.isNew());
		assertEquals("1", client.getId());
		assertEquals("localhost:1080", client.getBaseUrl());
		assertEquals("UTS", client.getCode());
		assertEquals("United Terminal Service", client.getName());
		Iterator<ClientDetail> it = client.getDetails().iterator();
		ClientDetail detail = it.next();
		assertNotEquals(detail, null);
		assertEquals(client.getDetails().stream().findFirst().get(), detail);
		assertEquals(client.getDetails().stream().findFirst().get().hashCode(), detail.hashCode());
		assertEquals(0, client.getDetails().stream().findFirst().get().compareTo(detail));
		assertEquals(client, detail.getClient());
		assertEquals(client.hashCode(), detail.getClient().hashCode());
		assertEquals(0, client.compareTo(detail.getClient()));
		assertEquals("1", detail.getId());
		assertEquals("Branch One", detail.getName());
		assertEquals("00011080", detail.getUcn());
	}
	
	protected Case caseRequest(Inventory inventory,
			Customer customer, User user) {
		var request = new Case();
		request.setCaseDate(1672462800000l);
		request.setCaseId("1");
		request.setCustomerUcn(customer.getLocationId());
		request.setUserId(user.getUserId());
		var assetIds = new TreeSet<String>();
		assetIds.add(inventory.getAssetId());
		request.setAssetIds(assetIds);
		return request;
	}
}
