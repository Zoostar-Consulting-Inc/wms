package net.zoostar.wms.web.controller;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class IndexController implements InitializingBean {

	@Value("${env.value}")
	protected String env;
	
	@Value("${message}")
	protected String message;
	
	@GetMapping
	public ModelAndView index(Map<String, String> model) {
		model.put("env", env);
		model.put("message", message);
		return new ModelAndView("index", model);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Loading {} in {} environment...", message, env);
	}
}
