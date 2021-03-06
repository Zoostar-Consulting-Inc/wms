package net.zoostar.wms.service.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.ClientDetailsRepository;
import net.zoostar.wms.dao.ClientRepository;
import net.zoostar.wms.entity.Client;
import net.zoostar.wms.service.ClientService;
import net.zoostar.wms.service.InventoryService;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

	@Autowired
	protected InventoryService inventoryManager;
	
	@Autowired
	protected ClientRepository clientRepository;
	
	@Autowired
	protected ClientDetailsRepository clientDetailsRepository;
	
	@Override
	public Client retrieveByUcn(String ucn) {
		var entity = clientDetailsRepository.findByUcn(ucn);
		if(entity.isEmpty()) {
			throw new EntityNotFoundException("No Client Details found for UCN: " + ucn);
		}
		var clientDetails = entity.get();
		var client = clientDetails.getClient();
		log.info("Retrieved client by UCN: {}", client);
		return client;
	}

}
