package net.zoostar.wms.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.entity.Client;

public interface ClientRepository extends PagingAndSortingRepository<Client, String> {
	Client findByCode(String code);
}
