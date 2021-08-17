package net.zoostar.wms.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.model.Client;

public interface ClientRepository  extends PagingAndSortingRepository<Client, String> {
	Client findByUcn(String ucn);
}
