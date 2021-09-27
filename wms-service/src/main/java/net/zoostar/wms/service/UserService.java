package net.zoostar.wms.service;

import net.zoostar.wms.entity.User;

public interface UserService {

	User retrieveByUserId(String userId);

}
