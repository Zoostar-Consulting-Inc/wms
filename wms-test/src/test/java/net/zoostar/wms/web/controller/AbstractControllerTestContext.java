package net.zoostar.wms.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.zoostar.wms.dao.ClientDetailsRepository;
import net.zoostar.wms.dao.ClientRepository;
import net.zoostar.wms.dao.CustomerRepository;
import net.zoostar.wms.dao.InventoryRepository;
import net.zoostar.wms.dao.SourceRepository;
import net.zoostar.wms.dao.UserRepository;

@WebAppConfiguration
@ActiveProfiles({"test"})
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-test.xml"})
public abstract class AbstractControllerTestContext<T> {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected ObjectMapper mapper;
	
	@MockBean
	protected CustomerRepository customerRepository;
	
	@MockBean
	protected InventoryRepository inventoryRepository;
	
	@MockBean
	protected UserRepository userRepository;
	
	@MockBean
	protected ClientRepository clientRepository;
	
	@MockBean
	protected ClientDetailsRepository clientDetailsRepository;
	
	@MockBean
	protected SourceRepository sourceRepository;
	
	@MockBean
	protected RestTemplate restTemplate;
	
	@Autowired
	protected WebApplicationContext wac;
	
	protected MockMvc mockMvc;
	
	@BeforeEach
	protected void beforeEach(TestInfo test) throws Exception {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

}
