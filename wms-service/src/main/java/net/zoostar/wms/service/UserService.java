package net.zoostar.wms.service;

import net.zoostar.wms.model.User;

public interface UserService {

	User retrieveByUserId(String userId);

}
