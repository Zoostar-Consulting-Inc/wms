package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.InventoryRepository;

@Slf4j
@WebAppConfiguration
@ActiveProfiles({"test"})
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-web.xml"})
class IndexControllerTest {
	
	@MockBean
	InventoryRepository inventoryRepository;

	@Autowired
	IndexController indexController;
	
	@Autowired
	WebApplicationContext wac;
	
	MockMvc mockMvc;
	
	@BeforeEach
	protected final void beforeEach(TestInfo test) throws Exception {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	void testIndex() throws Exception {
		//GIVEN
		String url = "/";
		
		//WHEN
	    var result = mockMvc.perform(get(url)).
	    		andExpect(view().name("index")).
	    		andExpect(model().attribute("env", "test")).
	    		andExpect(model().attribute("message", "Warehouse Management System (WMS)")).
	    		andReturn();
	    
		//THEN
		var response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

}
