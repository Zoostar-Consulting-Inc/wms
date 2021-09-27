package net.zoostar.wms.dao;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.entity.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, String> {
	Optional<Customer> findByEmail(String email);
	Collection<Customer> findByEmailStartsWith(String emailWildcard);
}
