package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.ClientDetail;
import net.zoostar.wms.model.Customer;
import net.zoostar.wms.model.Inventory;
import net.zoostar.wms.service.CaseService;
import net.zoostar.wms.service.TestDataRepositories;
import net.zoostar.wms.web.request.Case;
import net.zoostar.wms.web.request.OrderRequest;
import net.zoostar.wms.web.response.CaseResponse;

class OrderControllerTest extends AbstractControllerTestContext {

	@Autowired
	private TestDataRepositories<Inventory> inventories;
	
	@Autowired
	private TestDataRepositories<Client> clients;
	
	@Autowired
	protected TestDataRepositories<Case> cases;
	
	@Autowired
	protected TestDataRepositories<Customer> customers;
	
	@Autowired
	private CaseService caseManager;
	
	@Test
	void testOrderSubmitSuccess() throws Exception {
		//GIVEN
		var url = "/order/submit";
		
		//MOCK
		var testClientDetails = new HashMap<String, ClientDetail>();
		var client = clients.getRepository(Client.class).entrySet().stream().findFirst().get().getValue();
		log.info("Retrieved mocking client: {}", client);
		var clientDetails = client.getDetails();
		for(ClientDetail detail : clientDetails) {
			log.info("Setting mock client detail with client: {}", detail);
			detail.setClient(client);
			testClientDetails.put(detail.getUcn(), detail);
		}
		
		var caseEntry = cases.getRepository(Case.class).entrySet().stream().findFirst().get();
		var expected = caseEntry.getValue();
		for(var assetId : caseEntry.getValue().getAssetIds()) {
			var inventory = inventories.getRepository(Inventory.class).get(assetId);
			
			when(inventoryRepository.findByAssetId(assetId)).
					thenReturn(Optional.of(inventory));

			when(clientDetailsRepository.findByUcn(inventory.getHomeUcn())).
					thenReturn(Optional.of(testClientDetails.get(inventory.getHomeUcn())));
		}
		
		var entity = cases.getRepository(Case.class).
				entrySet().stream().findFirst();
		var inboundRequest = entity.get().getValue();
		var orders = caseManager.splitCase(inboundRequest);
		
		for(var order : orders) {
			when(orderSubmitManager.exchange(order.getUrl(),
					HttpMethod.POST, caseManager.getHeaders(), order, OrderRequest.class)).
						thenReturn(new ResponseEntity<OrderRequest>(order, HttpStatus.OK));
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
		var actual = mapper.readValue(response.getContentAsString(), CaseResponse.class);
		assertNotNull(actual);
		log.info("Response received: {}", actual);
		assertEquals(expected.getCaseId(), actual.getCaseId());
		assertEquals(expected.getCaseDate(), actual.getCaseDate());
		assertEquals(expected, actual);
		assertNotEquals(expected.getClass(), actual.getClass());
		assertEquals(expected.getCustomerUcn(), actual.getCustomerUcn());
		assertEquals(expected.getUserId(), actual.getUserId());
		for(String asseetId : expected.getAssetIds()) {
			assertTrue(actual.getResponses().containsKey(asseetId));
			assertEquals(HttpStatus.OK, actual.getResponses().get(asseetId));
		}
	}
}
