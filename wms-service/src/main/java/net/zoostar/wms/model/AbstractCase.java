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
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		AbstractCase other = (AbstractCase) that;
		return Objects.equals(caseId, other.caseId);
	}
}
