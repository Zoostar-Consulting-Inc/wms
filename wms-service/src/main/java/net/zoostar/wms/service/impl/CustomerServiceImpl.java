package net.zoostar.wms.service.impl;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.CustomerRepository;
import net.zoostar.wms.entity.Customer;
import net.zoostar.wms.entity.EntityWrapper;
import net.zoostar.wms.service.CustomerService;

@Slf4j
@Getter
@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl extends AbstractPersistableCrudService<EntityWrapper<Customer>, Customer> implements CustomerService {

	@Autowired
	protected CustomerRepository repository;

	@Override
	public Set<Customer> search(Set<String> searchTerms) {
		log.info("Search by: {}", searchTerms.toString());
		Set<Customer> customers = new LinkedHashSet<>();
		for(String searchTerm : searchTerms) {
			if(searchTerm.endsWith("*")) {
				customers.addAll(repository.findByEmailStartsWith(searchTerm.substring(0, searchTerm.length()-1)));
			} else {
				try {
					customers.add(retrieveByEmail(searchTerm));
				} catch(EntityNotFoundException e) {
					log.warn(e.getMessage());
				}
			}
		}
		return customers;
	}

	@Override
	public Customer retrieveByEmail(String email) {
		log.info("Search for email: {}...", email);
		var entity = repository.findByEmail(email);
		if(entity.isEmpty()) {
			throw new EntityNotFoundException("No customer found for email: " + email);
		}
		return entity.get();
	}

	@Override
	public Customer retrieveBySourceCodeAndSourceId(String sourceCode, String sourceId) {
		log.info("retrieveBySourceCodeAndSourceId(sourceCode: {}, sourceId: {}", sourceCode, sourceId);
		Optional<Customer> customer = repository.findBySourceCodeAndSourceId(sourceCode, sourceId);
		if(customer.isEmpty()) {
			throw new EntityNotFoundException(
					String.format("No customer found for sourceCode: [%s] and sourceId: [%s]", sourceCode, sourceId));
		}
		return customer.get();
	}

	@Override
	public Customer retrieveByKey(Customer customer) {
		return retrieveBySourceCodeAndSourceId(
				customer.getSourceCode(), customer.getSourceId());
	}

}
