package net.zoostar.wms.model;

import java.util.Objects;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class OrderRequest extends Case {
	
	private String url;

	public OrderRequest() {
		super();
	}
	
	public OrderRequest(String url, Case order) {
		this();
		this.url = url;
		setCaseDate(order.getCaseDate());
		setCaseId(order.getCaseId());
		setCustomerUcn(order.getCustomerUcn());
		setUserId(order.getUserId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(url);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		OrderRequest other = (OrderRequest) obj;
		return Objects.equals(url, other.url);
	}

}
