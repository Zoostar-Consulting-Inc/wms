package net.zoostar.wms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.api.inbound.UserSearchRequest;
import net.zoostar.wms.entity.User;
import net.zoostar.wms.service.StringPersistableCrudService;
import net.zoostar.wms.service.UserService;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController extends AbstractCrudRestController<User> {

	@Autowired
	private UserService userManager;

	@PostMapping(value = "/retrieve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> retrieveUserByUserId(@RequestBody UserSearchRequest request) {
		var user = userManager.retrieveByUserId(request.getUserId());
		log.info("User found: {}", user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@Override
	protected Class<User> getClazz() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StringPersistableCrudService<User> getCrudManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected User getPersistable(String sourceCode, String sourceId) {
		// TODO Auto-generated method stub
		return null;
	}
}
