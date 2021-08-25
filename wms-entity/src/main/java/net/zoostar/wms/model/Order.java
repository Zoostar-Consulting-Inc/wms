package net.zoostar.wms.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Order {

	private String caseId;
	
	private long caseDate;

	private String customerUcn;
	
	private String userId;
	
	private Map<String, Set<String>> urls;

	public Order(Case order) {
		this.caseId = order.getCaseId();
		this.caseDate = order.getCaseDate();
		this.customerUcn = order.getCustomerUcn();
		this.userId = order.getUserId();
		this.urls = new HashMap<>();
	}

	@Override
	public int hashCode() {
		return Objects.hash(caseId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Order)) {
			return false;
		}
		Order other = (Order) obj;
		return Objects.equals(caseId, other.caseId);
	}

}
