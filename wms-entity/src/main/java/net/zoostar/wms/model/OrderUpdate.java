package net.zoostar.wms.model;

import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderUpdate extends AbstractStringPersistable {

	private String caseId;
	
	private OrderStatus status;
	
	private Set<String> assetIds;
	
	@Override
	public int hashCode() {
		return Objects.hash(assetIds, caseId, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OrderUpdate)) {
			return false;
		}
		OrderUpdate other = (OrderUpdate) obj;
		return Objects.equals(assetIds, other.assetIds) && Objects.equals(caseId, other.caseId)
				&& status == other.status;
	}

}
