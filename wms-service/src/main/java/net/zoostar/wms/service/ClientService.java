package net.zoostar.wms.service;

import net.zoostar.wms.entity.Client;

public interface ClientService {
	Client retrieveByUcn(String ucn);
	Client retrieveByAssetId(String assetId);
}
