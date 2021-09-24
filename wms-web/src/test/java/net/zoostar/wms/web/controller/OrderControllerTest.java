package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.entity.Client;
import net.zoostar.wms.entity.ClientDetail;
import net.zoostar.wms.entity.Customer;
import net.zoostar.wms.entity.Inventory;
import net.zoostar.wms.service.OrderService;
import net.zoostar.wms.service.TestDataRepositories;
import net.zoostar.wms.service.response.OrderResponse;
import net.zoostar.wms.web.request.OrderRequest;

class OrderControllerTest extends AbstractControllerTestContext {

	@Autowired
	private TestDataRepositories<Inventory> inventories;
	
	@Autowired
	private TestDataRepositories<Client> clients;
	
	@Autowired
	protected TestDataRepositories<OrderRequest> orders;
	
	@Autowired
	protected TestDataRepositories<Customer> customers;
	
	@Autowired
	private OrderService orderManager;
	
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
		log.info("Retrieved mocking client: {}", client);
		var clientDetails = client.getDetails();
		ClientDetail detailPrevious = null;
		ClientDetail detailCurrent = null;
		for(ClientDetail detail : clientDetails) {
			detail.setClient(client);
			log.info("Setting mock client detail with client: {}", detail);
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
		for(var order : orders.entrySet()) {
			when(restTemplate.exchange(order.getValue().getUrl(),
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
	    Collection<OrderResponse> orderResponses = mapper.readValue(response.getContentAsString(),
	    		mapper.getTypeFactory().constructCollectionType(Collection.class, OrderResponse.class));
	    assertNotNull(orderResponses);
	    for(OrderResponse actual : orderResponses) {
	    	assertNotNull(actual);
	    }

//	    for(String assetId : expected.getAssetIds()) {
//			assertTrue(actual.getResponses().containsKey(asseetId));
//			assertEquals(HttpStatus.OK, actual.getResponses().get(asseetId));
//		}
//		
//		var sameCase = actual;
//		assertEquals(sameCase, actual);

		assertEquals("1", client.getId());
		assertEquals("United Terminal Service", client.getName());
		assertFalse(client.isNew());
		assertNotNull(client.toString());
		
		var clientNext = new Client();
		clientNext.setCode("PEDEX");
		assertTrue(clientNext.isNew());
		assertTrue("Expected: " + client.compareTo(clientNext), client.compareTo(clientNext) > 0);
	}
}
