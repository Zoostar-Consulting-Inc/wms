package net.zoostar.wms.api.inbound;

import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString()
public class OrderUpdateRequest {

	private String ucn;
	
	private String caseId;
	
	private String status;
	
	Set<String> assetIds;

	@Override
	public int hashCode() {
		return Objects.hash(assetIds, caseId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OrderUpdateRequest)) {
			return false;
		}
		OrderUpdateRequest other = (OrderUpdateRequest) obj;
		return Objects.equals(assetIds, other.assetIds) && Objects.equals(caseId, other.caseId);
	}
}
