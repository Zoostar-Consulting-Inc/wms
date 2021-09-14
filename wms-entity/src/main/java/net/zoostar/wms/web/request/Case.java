package net.zoostar.wms.web.request;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import net.zoostar.wms.model.AbstractCase;

@Getter
@Setter
public class Case extends AbstractCase {
	
	private Set<String> assetIds;
	
	public Case() {
		super();
		this.assetIds = new HashSet<>();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object that) {
		return super.equals(that);
	}

}
