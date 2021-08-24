package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.JavaType;

import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.model.ClientDetail;
import net.zoostar.wms.model.Customer;
import net.zoostar.wms.model.Inventory;
import net.zoostar.wms.model.User;
import net.zoostar.wms.service.Repositories;

public class OrderControllerTest extends AbstractControllerTestContext {

	@Autowired
	private Repositories<Inventory> inventories;

	@Autowired
	private Repositories<Customer> customers;

	@Autowired
	private Repositories<User> users;
	
	@Autowired
	private Repositories<Client> clients;
	
	@Test
	void testOrderSubmitSuccess() throws Exception {
		//GIVEN
		var inventory = inventories.getRepository(Inventory.class).entrySet().stream().findFirst().get().getValue();
		var customer = customers.getRepository(Customer.class).entrySet().stream().findFirst().get().getValue();
		var user = users.getRepository(User.class).entrySet().stream().findFirst().get().getValue();
		var client = clients.getRepository(Client.class).entrySet().stream().findFirst().get().getValue();
		var request = caseRequest(inventory, customer, user);
		var url = "/order/submit";
		
		//MOCK
		var clientDetails = client.getDetails();
		for(ClientDetail detail : clientDetails) {
			detail.setClient(client);
		}
		
		when(inventoryRepository.findByAssetId(inventory.getAssetId())).
				thenReturn(Optional.of(inventory));
		
		when(clientDetailsRepository.findByUcn(inventory.getHomeUcn())).
				thenReturn(clientDetails.stream().findFirst());
		
		//WHEN
	    var result = mockMvc.perform(post(url).
	    		contentType(MediaType.APPLICATION_JSON_VALUE).
	    		content(mapper.writeValueAsString(request)).
	    		accept(MediaType.APPLICATION_JSON_VALUE)).
	    		andReturn();
	    
	    //THEN
	    assertNotNull(result);
	    var response = result.getResponse();
	    assertNotNull(response);
		JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Case.class);
		var actual = mapper.readValue(response.getContentAsString(), javaType);
		assertNotNull(actual);
		log.info("Actual: {}", actual.toString());
	}
	
	protected Case caseRequest(Inventory inventory,
			Customer customer, User user) {
		var request = new Case();
		request.setCaseDate(1672462800000l);
		request.setCaseId("1");
		request.setCustomerUcn(customer.getLocationId());
		request.setUserId(user.getUserId());
		var assetIds = new TreeSet<String>();
		assetIds.add("FE18888");
		request.setAssetIds(assetIds);
		return request;
	}
}
