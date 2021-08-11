package net.zoostar.wms.web.request;

import java.util.SortedSet;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InventorySearchRequest {

	private SortedSet<String> searchTerms = new TreeSet<>();
	
}
