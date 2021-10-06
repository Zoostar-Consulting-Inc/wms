package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.api.inbound.OrderRequest;
import net.zoostar.wms.api.inbound.OrderUpdateRequest;
import net.zoostar.wms.api.outbound.Order;
import net.zoostar.wms.entity.Client;
import net.zoostar.wms.entity.ClientDetail;
import net.zoostar.wms.entity.Inventory;
import net.zoostar.wms.service.OrderService;
import net.zoostar.wms.service.TestDataRepositories;

class OrderControllerTest extends AbstractControllerTestContext {

	@Autowired
	private TestDataRepositories<Inventory> inventories;
	
	@Autowired
	private TestDataRepositories<Client> clients;
	
	@Autowired
	private OrderService orderManager;
	
	@Value("${order.update.server.url")
	protected String orderUpdateServerUrl;

	@Test
	void testOrderSubmitSuccess() throws Exception {
		//GIVEN
		var url = "/order/submit";
		
		var caseDate = System.currentTimeMillis() + 86400000l;
		var caseId = "1";
		var customerUcn = "01386585";
		var userId = "user1@email.com";
		Set<String> sortedAssetIds = new TreeSet<>();
		sortedAssetIds.add("FE38888");
		sortedAssetIds.add("FE28888");
		sortedAssetIds.add("FE18888");
		
		var inboundRequest = new OrderRequest();
		inboundRequest.setCaseDate(caseDate);
		inboundRequest.setCaseId(caseId);
		inboundRequest.setCustomerUcn(customerUcn);
		inboundRequest.setUserId(userId);
		inboundRequest.setAssetIds(sortedAssetIds);
		
		//MOCK
		var testClientDetails = new HashMap<String, ClientDetail>();
		var client = clients.getRepository(Client.class).entrySet().stream().findFirst().get().getValue();
		log.info("Retrieved mocking client: {}", client.toString());
		var clientDetails = client.getDetails();
		ClientDetail detailPrevious = null;
		ClientDetail detailCurrent = null;
		for(ClientDetail detail : clientDetails) {
			detail.setClient(client);
			log.info("Setting mock client with client details: {}", detail);
			testClientDetails.put(detail.getUcn(), detail);
			assertNotEquals(detail, detailPrevious);
			if(detailPrevious != null) {
				assertTrue("Expected: " + detailPrevious.compareTo(detail),
						detailPrevious.compareTo(detail) < 0);
			}
			detailPrevious = detail;
			detailCurrent = detail;
		}
		assertEquals(detailPrevious, detailCurrent);

		for(String assetId : sortedAssetIds) {
			var inventory = inventories.getRepository(Inventory.class).get(assetId);
			
			when(inventoryRepository.findByAssetId(assetId)).
					thenReturn(Optional.of(inventory));
		
			when(clientDetailsRepository.findByUcn(inventory.getHomeUcn())).
					thenReturn(Optional.of(testClientDetails.get(inventory.getHomeUcn())));
		}
		
		var orders = orderManager.splitOrder(inboundRequest);
		log.info("Order split into: {}", orders.size());
		Order orderPrevious = null;
		for(var order : orders.entrySet()) {
			assertNotEquals(order.getValue(), orderPrevious);
			assertNotEquals(order.getValue().hashCode(), orderPrevious == null ? 0l : orderPrevious.hashCode());
			orderPrevious = order.getValue();
			assertEquals(order.getValue(), orderPrevious);
			when(restTemplate.exchange(order.getValue().getClient().getBaseUrl(),
					HttpMethod.POST, new HttpEntity<>(order.getValue(), orderManager.getHeaders()), OrderRequest.class)).
						thenReturn(new ResponseEntity<OrderRequest>(order.getValue(), HttpStatus.OK));
		}
		
		//WHEN
	    var result = mockMvc.perform(post(url).
	    		contentType(MediaType.APPLICATION_JSON_VALUE).
	    		content(mapper.writeValueAsString(inboundRequest)).
	    		accept(MediaType.APPLICATION_JSON_VALUE)).
	    		andReturn();
	    
	    //THEN
	    assertNotEquals(inboundRequest, null);
	    assertNotNull(result);
	    var response = result.getResponse();
	    assertNotNull(response);
	    assertEquals(HttpStatus.OK.value(), response.getStatus());
	    log.debug("Raw response: {}", response.getContentAsString());
	    OrderRequest actual = mapper.readValue(
	    		response.getContentAsString(), OrderRequest.class);
	    assertNotNull(actual);
    	assertNotNull(actual.toString());
    	assertEquals(caseDate, actual.getCaseDate());
    	assertEquals(caseId, actual.getCaseId());
    	assertEquals(customerUcn, actual.getCustomerUcn());
    	assertEquals(userId, actual.getUserId());
    	assertEquals(sortedAssetIds, actual.getAssetIds());

		assertEquals("1", client.getId());
		assertEquals("United Terminal Service", client.getName());
		assertFalse(client.isNew());
		assertNotNull(client.toString());
		
		var clientNext = new Client();
		clientNext.setCode("PEDEX");
		assertTrue(clientNext.isNew());
		assertNotEquals(client, clientNext);
		assertTrue("Expected: " + client.compareTo(clientNext), client.compareTo(clientNext) > 0);
		assertNotNull(clientNext.toString());
	}
	
	@Test
	void testOrderUpdateSuccess() throws Exception {
		//GIVEN
		String url = "/order/update";
		var request = new OrderUpdateRequest();
		request.setCaseId("1");
		request.setStatus("O");
		request.setUcn("00011080");
		Set<String> assetIds = new TreeSet<>();
		assetIds.add("FE28888");
		assetIds.add("FE18888");
		request.setAssetIds(assetIds);
		
		//MOCK-WHEN
		when(restTemplate.exchange(orderUpdateServerUrl,
				HttpMethod.POST, new HttpEntity<>(request, orderManager.getHeaders()), OrderUpdateRequest.class)).
					thenReturn(new ResponseEntity<OrderUpdateRequest>(request, HttpStatus.OK));

		var result = mockMvc.perform(post(url).
	    		contentType(MediaType.APPLICATION_JSON_VALUE).
	    		content(mapper.writeValueAsString(request)).
	    		accept(MediaType.APPLICATION_JSON_VALUE)).
	    		andReturn();
		
		//THEN
		assertNotNull(result);
	    var response = result.getResponse();
	    assertNotNull(response);
	    assertEquals(HttpStatus.OK.value(), response.getStatus());
	    OrderUpdateRequest actual = mapper.readValue(response.getContentAsString(), OrderUpdateRequest.class);
	    log.info("Response received: {}", actual.toString());
		assertEquals(request, actual);
		assertEquals(request.hashCode(), actual.hashCode());
		
		var other = new OrderUpdateRequest();
		other.setCaseId("2");
		assertNotEquals(other, actual);
		
		var same = actual;
		assertEquals(same, actual);
		assertNotEquals(same, null);
	}
}
