package net.zoostar.wms.web.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.wms.entity.Customer;
import net.zoostar.wms.entity.EntityWrapper;

@Getter
@Setter
@ToString
public class CustomerRequest implements EntityWrapper<Customer> {

	private String sourceCode;
	
	private String sourceId;
	
	private String email;

	public CustomerRequest() {
		super();
	}
	
	public CustomerRequest(Customer entity) {
		this();
		this.sourceCode = entity.getSourceCode();
		this.sourceId = entity.getSourceId();
		this.email = entity.getEmail();
	}

	@Override
	public Customer toEntity() {
		var entity = new Customer();
		entity.setSourceCode(sourceCode);
		entity.setSourceId(sourceId);
		entity.setEmail(email);
		return entity;
	}

	@Override
	public Class<Customer> getClazz() {
		return Customer.class;
	}

}
