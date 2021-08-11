package net.zoostar.wms.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class IndexControllerTest {

	IndexController indexController;
	
	@BeforeEach
	protected void beforeEach(TestInfo test) throws Exception {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());
		indexController = new IndexController();
		indexController.setEnv("test");
		indexController.setMessage("Warehouse Management System (WMS)");
		indexController.afterPropertiesSet();
	}
	
	@Test
	void testIndex() throws Exception {
		//GIVEN
		var model = new ModelMap();
		
		//WHEN
		ModelAndView modelAndView = indexController.index(model);
		
		//THEN
		assertNotNull(modelAndView);
		assertEquals("index", modelAndView.getViewName());
		model = modelAndView.getModelMap();
		assertEquals(2, model.size());
		assertEquals("test", model.get("env"));
		assertEquals("Warehouse Management System (WMS)", model.get("message"));
	}

}
