package net.zoostar.wms.model;

import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Case {

	private String caseId;
	
	private long caseDate;

	private String customerUcn;
	
	private String userId;
	
	private Set<String> assetIds;

	@Override
	public int hashCode() {
		return Objects.hash(caseId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Case)) {
			return false;
		}
		Case other = (Case) obj;
		return Objects.equals(caseId, other.caseId);
	}
}
