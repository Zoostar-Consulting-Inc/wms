package net.zoostar.wms.service.impl;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.CustomerRepository;
import net.zoostar.wms.entity.Customer;
import net.zoostar.wms.service.CustomerService;

@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	protected CustomerRepository customerRepository;

	@Override
	@Transactional(readOnly = true)
	public Set<Customer> search(Set<String> searchTerms) {
		log.info("Search by: {}", searchTerms.toString());
		Set<Customer> customers = new LinkedHashSet<>();
		for(String searchTerm : searchTerms) {
			if(searchTerm.endsWith("*")) {
				customers.addAll(customerRepository.findByEmailStartsWith(searchTerm.substring(0, searchTerm.length()-1)));
			} else {
				try {
					customers.add(retrieveByEmail(searchTerm));
				} catch(NoSuchElementException e) {
					log.warn(e.getMessage());
				}
			}
		}
		return customers;
	}

	@Override
	@Transactional(readOnly = true)
	public Customer retrieveByEmail(String email) {
		log.info("Search for email: {}...", email);
		var entity = customerRepository.findByEmail(email);
		if(entity.isEmpty()) {
			throw new NoSuchElementException("No customer found for email: " + email);
		}
		return entity.get();
	}

	@Override
	@Transactional(readOnly = true)
	public Customer retrieveBySourceCodeAndSourceId(String sourceCode, String sourceId) {
		Optional<Customer> customer = customerRepository.findBySourceCodeAndSourceId(sourceCode, sourceId);
		if(customer.isEmpty()) {
			throw new NoSuchElementException(
					String.format("No customer found for sourceCode: [%s] and sourceId: [%s]", sourceCode, sourceId)
			);
		}
		return customer.get();
	}

	@Override
	public Customer create(Customer customer) {
		Customer entity = null;
		try {
			entity = retrieveBySourceCodeAndSourceId(customer.getSourceCode(), customer.getSourceId());
			throw new EntityExistsException("Entity exists for Source Code and Id: " +
					customer.getSourceCode() + " " + customer.getSourceId());
		} catch(NoSuchElementException e) {
			entity = customerRepository.save(customer);
		}
		return entity;
	}

	@Override
	public Customer update(Customer customer) {
		if(customer.isNew()) {
			throw new NoSuchElementException(String.format(
					"No entity found to update for: %s", customer.toString()));
		} else {
			return customerRepository.save(customer);
		}
	}

	@Override
	public void delete(String id) {
		customerRepository.deleteById(id);
	}
	
}
