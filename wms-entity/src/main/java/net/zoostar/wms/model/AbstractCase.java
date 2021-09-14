package net.zoostar.wms.model;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AbstractCase {

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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractCase other = (AbstractCase) obj;
		return Objects.equals(caseId, other.caseId);
	}
}
