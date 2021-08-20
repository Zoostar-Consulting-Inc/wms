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
public class SplitOrder extends AbstractStringPersistable {

	private String caseId;
	
	private long caseDate;

	private String customerUcn;
	
	private String userId;
	
	private Map<Client, Set<String>> clients;

	public SplitOrder(Case order) {
		this.caseId = order.getCaseId();
		this.caseDate = order.getCaseDate();
		this.customerUcn = order.getCustomerUcn();
		this.userId = order.getUserId();
		this.clients = new HashMap<>();
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
		if (!(obj instanceof SplitOrder)) {
			return false;
		}
		SplitOrder other = (SplitOrder) obj;
		return Objects.equals(caseId, other.caseId);
	}
}
