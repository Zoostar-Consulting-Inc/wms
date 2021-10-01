package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import net.zoostar.wms.api.inbound.UserSearchRequest;
import net.zoostar.wms.entity.User;
import net.zoostar.wms.service.TestDataRepositories;

class UserControllerTest extends AbstractControllerTestContext {
	
	@Autowired
	protected TestDataRepositories<User> repositories;
	
	@Test
	void testRetrieveUserByUserId() throws Exception {
		//GIVEN
		var url = "/user/retrieve";
		var entity = repositories.getRepository(User.class).
				entrySet().stream().findFirst();
		var expected = entity.get().getValue();
		String userId = expected.getId();
		var request = new UserSearchRequest(userId);
		
		//MOCK
		when(userRepository.findByUserId(userId)).
			thenReturn(Optional.of(expected));
		
		//WHEN
		var result = mockMvc.perform(post(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(request))).
				andReturn();

		//THEN
		assertNotNull(result);
		log.info("Result: {}", result);
		var response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		User actual = mapper.readValue(response.getContentAsString(), User.class);
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.getUserId(), actual.getUserId());
		assertEquals(0, expected.compareTo(actual));
		assertNotNull(actual.toString());
		
		var unexpected = repositories.getRepository(User.class).
				entrySet().stream().findAny().get().getValue();
		unexpected.setSource("xyz");
		assertNotEquals(unexpected, actual);
	}

	@Test
	void testNoSuchElementException() throws Exception {
		//GIVEN
		var url = "/user/retrieve";
		var entity = repositories.getRepository(User.class).
				entrySet().stream().findFirst();
		var expected = entity.get().getValue();
		String userId = expected.getId();
		var request = new UserSearchRequest(userId);
		
		//MOCK
		when(userRepository.findByUserId(expected.getUserId())).
			thenReturn(Optional.empty());
		
		//WHEN
		var result = mockMvc.perform(post(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(request))).
				andReturn();

		//THEN
		assertNotNull(result);
		log.info("Result: {}", result);
		var response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

}
