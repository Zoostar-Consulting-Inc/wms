package net.zoostar.wms.service.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.CustomerRepository;
import net.zoostar.wms.model.Customer;
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
		Set<Customer> customers = new TreeSet<>();
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
	
}
