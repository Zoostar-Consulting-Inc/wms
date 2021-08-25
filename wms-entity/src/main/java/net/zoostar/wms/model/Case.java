package net.zoostar.wms.model;

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

}
