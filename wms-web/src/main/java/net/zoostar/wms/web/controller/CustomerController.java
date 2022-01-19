package net.zoostar.wms.web.controller;

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
import net.zoostar.wms.service.StringPersistableCrudService;
import net.zoostar.wms.web.request.CustomerRequest;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController extends AbstractCrudRestController<CustomerRequest, Customer> {

	@Autowired
	protected CustomerService customerManager;
	
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
	
	@Override
	@PostMapping(value = "/update/{sourceCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> update(@PathVariable String sourceCode, @RequestParam String sourceId) {
		return super.update(sourceCode, sourceId);
	}

	@Override
	protected StringPersistableCrudService<Customer> getCrudManager() {
		return this.customerManager;
	}

	@Override
	protected CustomerRequest toEntityWrapper(String sourceCode, String sourceId) {
		var customer = new CustomerRequest();
		customer.setSourceCode(sourceCode);
		customer.setSourceId(sourceId);
		return customer;
	}

	@Override
	protected Class<CustomerRequest> getEntityWrapperClazz() {
		return CustomerRequest.class;
	}

}
