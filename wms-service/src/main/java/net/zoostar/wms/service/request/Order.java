package net.zoostar.wms.service.request;

import java.util.Objects;

import lombok.Getter;
import lombok.ToString;
import net.zoostar.wms.web.request.OrderRequest;

@Getter
@ToString(callSuper = true)
public class Order extends OrderRequest {
	
	private String url;

	public Order() {
		super();
	}
	
	public Order(String url, OrderRequest order) {
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
		Order other = (Order) obj;
		return Objects.equals(url, other.url);
	}

}
