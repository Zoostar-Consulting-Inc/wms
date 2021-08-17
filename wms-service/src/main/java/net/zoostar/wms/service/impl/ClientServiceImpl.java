package net.zoostar.wms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.zoostar.wms.dao.ClientRepository;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	protected ClientRepository clientRepository;
	
	@Override
	public Client retrieve(String ucn) {
		return clientRepository.findByUcn(ucn);
	}

}
