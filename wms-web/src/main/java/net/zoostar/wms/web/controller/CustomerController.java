package net.zoostar.wms.web.controller;

import java.util.NoSuchElementException;
import java.util.Set;

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
		ResponseEntity<Customer> response = null;
		Customer customer = null;
		try {
			customer = customerManager.retrieveBySourceCodeAndSourceId(sourceCode, sourceId);
			response = update(customer);
		} catch(NoSuchElementException e) {
			log.info(e.getMessage());
			customer = create(sourceCode, sourceId);
			response = new ResponseEntity<>(customer, HttpStatus.CREATED);
		}
		postUpdateListener(customer);
		return response;
	}

	protected void postUpdateListener(Customer customer) {
		
	}

	protected Customer create(String sourceCode, String sourceId) {
		Customer customer = null;
		var response = sourceManager.retrieve(sourceCode, sourceId, Customer.class);
		if(response.getStatusCode() == HttpStatus.OK) {
			customer = response.getBody();
			customer = customerManager.create(customer);
		}
		return customer;
	}

	protected ResponseEntity<Customer> update(Customer customer) {
		ResponseEntity<Customer> response = null;
		response = sourceManager.retrieve(
				customer.getSourceCode(), customer.getSourceId(), Customer.class);
		if(response.getStatusCode() == HttpStatus.OK) {
			response.getBody().setId(customer.getId());
			response = new ResponseEntity<>(customerManager.update(
					response.getBody()), HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(delete(customer), HttpStatus.OK);
		}
		return response;
	}

	protected Customer delete(Customer customer) {
		return customerManager.delete(customer.getId());
	}
}
