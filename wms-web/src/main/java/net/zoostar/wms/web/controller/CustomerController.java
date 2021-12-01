package net.zoostar.wms.web.controller;

import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.api.inbound.CustomerSearchRequest;
import net.zoostar.wms.entity.Customer;
import net.zoostar.wms.service.CustomerService;
import net.zoostar.wms.service.SourceService;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController extends AbstractCommonErrorHandler<Customer> {

	@Autowired
	protected CustomerService customerManager;
	
	@Autowired
	private SourceService<Customer> sourceManager;
	
	@GetMapping(value = "/retrieve/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> retrieveByEmail(@PathVariable String email) {
		log.info("API triggered: /retrieve/{}", email);
		return new ResponseEntity<>(customerManager.retrieveByEmail(email), HttpStatus.OK);
	}
	
	@PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Customer>> search(@RequestBody CustomerSearchRequest request) {
		log.info("{}", "API triggered: /customer/search");
		return new ResponseEntity<>(customerManager.search(request.getSearchTerms()), HttpStatus.OK);
	}
	
	@PostMapping(value = "/update/{sourceCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> update(@PathVariable String sourceCode, @RequestParam String sourceId) {
		Customer entity = null;
		ResponseEntity<Customer> response = null;
		try {
			entity = customerManager.retrieveBySourceCodeAndSourceId(sourceCode, sourceId);
			response = new ResponseEntity<>(update(entity), HttpStatus.OK);
		} catch(EntityNotFoundException e) {
			log.info(e.getMessage());
			entity = create(sourceCode, sourceId);
			response = new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
		postUpdateListener(entity);
		return response;
	}

	protected Customer create(String sourceCode, String sourceId) {
		return customerManager.create(
				sourceManager.retrieve(sourceCode, sourceId, Customer.class));
	}

	protected Customer update(final Customer entity) {
		Customer customer = null;
		try {
			customer = sourceManager.retrieve(
					entity.getSourceCode(), entity.getSourceId(), Customer.class);
			customer.setId(entity.getId());
			customer = customerManager.update(customer);
		} catch(EntityNotFoundException e) {
			log.info(e.getMessage());
			customer = delete(entity);
		}
		return customer;
	}

	protected Customer delete(Customer customer) {
		return customerManager.delete(customer.getId());
	}

	protected void postUpdateListener(Customer customer) {
		// To be implemented, if needed, by extending classes
	}
}
