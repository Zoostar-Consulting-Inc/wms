package net.zoostar.wms.web.controller;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Controller
public class IndexController implements InitializingBean {

	@Value("${env.value}")
	protected String env;
	
	@Value("${message}")
	protected String message;
	
	@Autowired
	protected Properties properties;
	
	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView index(Map<String, Object> model) {
		log.info("{}", "Loading index page...");
		model.put("env", env);
		model.put("message", message);
		return new ModelAndView("index", model);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Loading {} in {} environment with following properties...", message, env);
		var keys = properties.propertyNames();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			log.info("{}: {}", key, properties.get(key));
		}
	}
}
