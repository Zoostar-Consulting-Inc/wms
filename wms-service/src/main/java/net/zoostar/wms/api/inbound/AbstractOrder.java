package net.zoostar.wms.api.inbound;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AbstractOrder {

	private String caseId;
	
	private long caseDate;

	private String customerUcn;
	
	private String userId;

	@Override
	public int hashCode() {
		return Objects.hash(caseId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractOrder)) {
			return false;
		}
		AbstractOrder other = (AbstractOrder) obj;
		return Objects.equals(caseId, other.caseId);
	}
}
