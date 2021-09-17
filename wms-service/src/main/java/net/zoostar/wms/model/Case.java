package net.zoostar.wms.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Case extends AbstractCase {
	
	private Set<String> assetIds;
	
	public Case() {
		super();
		this.assetIds = new HashSet<>();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(assetIds);
		return result;
	}
	
	@Override
	public boolean equals(Object that) {
		if (!super.equals(that)) {
			return false;
		}
		Case other = (Case) that;
		return Objects.equals(assetIds, other.assetIds);
	}

}
