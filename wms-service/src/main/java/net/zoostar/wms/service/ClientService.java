package net.zoostar.wms.service;

import net.zoostar.wms.model.Client;

public interface ClientService {
	Client retrieveByUcn(String ucn);
}
