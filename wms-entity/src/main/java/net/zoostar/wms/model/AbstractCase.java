package net.zoostar.wms.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractCase {

	@EqualsAndHashCode.Include
	private String caseId;
	
	private long caseDate;

	private String customerUcn;
	
	private String userId;

}
