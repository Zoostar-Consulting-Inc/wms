package net.zoostar.wms.api.inbound;

import java.util.SortedSet;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchRequest {

	private SortedSet<String> searchTerms = new TreeSet<>();

}
