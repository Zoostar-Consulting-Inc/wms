package net.zoostar.wms.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.zoostar.wms.dao.ClientDetailsRepository;
import net.zoostar.wms.dao.ClientRepository;
import net.zoostar.wms.model.Client;
import net.zoostar.wms.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	protected ClientRepository clientRepository;
	
	@Autowired
	protected ClientDetailsRepository clientDetailsRepository;
	
	@Override
	public Client retrieveByUcn(String ucn) {
		var entity = clientDetailsRepository.findByUcn(ucn);
		if(entity.isEmpty()) {
			throw new NoSuchElementException("No Client Details found for UCN: " + ucn);
		}
		var clientDetails = entity.get();
		return clientDetails.getClient();
	}

}
