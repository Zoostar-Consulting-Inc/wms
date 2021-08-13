package net.zoostar.wms.web.request;

import java.util.SortedSet;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventorySearchRequest {

	private SortedSet<String> searchTerms = new TreeSet<>();
	
}
