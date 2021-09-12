package net.zoostar.wms.web.request;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.wms.model.AbstractCase;

@Getter
@Setter
@ToString
public class Case extends AbstractCase {
	
	private Set<String> assetIds;
	
	public Case() {
		super();
		this.assetIds = new HashSet<>();
	}

}
