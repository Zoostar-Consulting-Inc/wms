package net.zoostar.wms.service;

import java.util.Set;

import net.zoostar.wms.entity.Customer;

public interface CustomerService {
	Customer create(Customer customer);
	Customer retrieveByEmail(String email);
	Set<Customer> search(Set<String> searchTerms);
	Customer retrieveBySourceCodeAndSourceId(String sourceCode, String sourceId);
	Customer update(Customer customer);
	void delete(String id);
}
