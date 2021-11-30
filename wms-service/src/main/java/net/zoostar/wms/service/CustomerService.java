package net.zoostar.wms.service;

import java.util.Set;

import net.zoostar.wms.entity.Customer;

public interface CustomerService extends StringPersistableCrudService<Customer> {
	Customer retrieveByEmail(String email);
	Set<Customer> search(Set<String> searchTerms);
	Customer retrieveBySourceCodeAndSourceId(String sourceCode, String sourceId);
}
