package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import net.zoostar.wms.model.User;
import net.zoostar.wms.web.request.UserSearchRequest;

public class UserControllerTest extends AbstractMockBeanTestContext<User> {

	@Test
	void testRetrieveUserByUserId() throws Exception {
		//GIVEN
		var url = "/user/retrieve";
		var expected = entities.get(0);
		var request = new UserSearchRequest(expected.getUserId());
		
		//MOCK
		when(userRepository.findByUserId(expected.getUserId())).
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
		assertEquals(expected.getUserId(), actual.getUserId());
	}

	@Test
	void testNoSuchElementException() throws Exception {
		//GIVEN
		var url = "/user/retrieve";
		var expected = entities.get(0);
		var request = new UserSearchRequest(expected.getUserId());
		
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
	
	@Override
	protected String getPath() {
		return "data/user.json";
	}

	@Override
	protected Class<User> getClazz() {
		return User.class;
	}

}
