package net.zoostar.wms.model;

import java.util.HashSet;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order extends Case {
	
	private String url;
	
	public Order(String url, Case order) {
		this.url = url;
		setCaseDate(order.getCaseDate());
		setCaseId(order.getCaseId());
		setCustomerUcn(order.getCustomerUcn());
		setUserId(order.getUserId());
		this.setAssetIds(new HashSet<>());
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
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(url, other.url);
	}

	@Override
	public String toString() {
		return "Order [url=" + url + ", toString()=" + super.toString() + "]";
	}

}
