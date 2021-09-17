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
import net.zoostar.wms.entity.User;
import net.zoostar.wms.service.UserService;
import net.zoostar.wms.web.request.UserSearchRequest;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController extends AbstractCommonErrorHandler<User> {

	@Autowired
	private UserService userManager;

	@PostMapping(value = "/retrieve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> retrieveUserByUserId(@RequestBody UserSearchRequest request) {
		var user = userManager.retrieveByUserId(request.getUserId());
		log.info("User found: {}", user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
