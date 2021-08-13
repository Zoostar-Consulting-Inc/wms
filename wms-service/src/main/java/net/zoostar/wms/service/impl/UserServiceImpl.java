package net.zoostar.wms.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zoostar.wms.dao.UserRepository;
import net.zoostar.wms.model.User;
import net.zoostar.wms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	protected UserRepository userRepository;
	
	@Override
	public User retrieveByUserId(String userId) {
		var entity = userRepository.findByUserId(userId);
		if(entity.isEmpty()) {
			throw new NoSuchElementException("No User found for user id: " + userId);
		}
		return entity.get();
	}

}
