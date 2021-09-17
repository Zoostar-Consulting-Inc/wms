package net.zoostar.wms.dao;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.entity.ClientDetail;

public interface ClientDetailsRepository extends PagingAndSortingRepository<ClientDetail, String> {
	Optional<ClientDetail> findByUcn(String ucn);
}
