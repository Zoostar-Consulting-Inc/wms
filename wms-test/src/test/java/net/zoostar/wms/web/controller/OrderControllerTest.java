package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import net.zoostar.wms.model.Case;
import net.zoostar.wms.service.OrderService;
import net.zoostar.wms.web.request.CaseOrderSubmitRequest;

class OrderControllerTest extends AbstractMockBeanTestContext<Case> {

	@Autowired
	OrderService orderManager;
	
	@Test
	void testSubmitOrder() throws Exception {
		//GIVEN
		var url = "/order";
		var expected = entities.get(0);
		var request = new CaseOrderSubmitRequest(expected);
		var entity = new HttpEntity<>(request.toEntity(), orderManager.getHeaders());
		
		//MOCK
		when(restTemplate.exchange("localhost", HttpMethod.POST, entity, Case.class)).
				thenReturn(new ResponseEntity<Case>(request.toEntity(), HttpStatus.OK));
		
		//WHEN
		var result = mockMvc.perform(post(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(request))).
				andReturn();

		//THEN
		assertNotNull(result);
		log.info("Result: {}", result.toString());
		var response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		log.info("Response: {}", response.toString());
		var actual = mapper.readValue(response.getContentAsString(), Case.class);
		assertEquals(expected, actual);
		assertTrue(actual.equals(actual));
		assertFalse(actual.equals(request));
		assertEquals(expected.hashCode(), actual.hashCode());
		assertNotNull(actual.toString());
	}
	
	@Override
	protected String getPath() {
		return "data/case.json";
	}

	@Override
	protected Class<Case> getClazz() {
		return Case.class;
	}

}
