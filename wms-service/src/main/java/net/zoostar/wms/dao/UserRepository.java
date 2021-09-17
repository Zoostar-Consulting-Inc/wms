package net.zoostar.wms.dao;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
	Optional<User> findByUserId(String userId);
}
