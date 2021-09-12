package net.zoostar.wms.web.request;

import java.util.HashSet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class OrderRequest extends Case {
	
	private String url;
	
	public OrderRequest() {
		super();
	}
	
	public OrderRequest(String url, Case order) {
		super();
		this.url = url;
		setCaseDate(order.getCaseDate());
		setCaseId(order.getCaseId());
		setCustomerUcn(order.getCustomerUcn());
		setUserId(order.getUserId());
		this.setAssetIds(new HashSet<>());
	}

}
