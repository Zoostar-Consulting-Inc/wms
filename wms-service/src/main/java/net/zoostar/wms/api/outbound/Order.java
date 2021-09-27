package net.zoostar.wms.api.outbound;

import java.util.Objects;

import lombok.Getter;
import lombok.ToString;
import net.zoostar.wms.api.inbound.OrderRequest;
import net.zoostar.wms.entity.Client;

@Getter
@ToString(callSuper = true)
public class Order extends OrderRequest {
	
	private Client client;

	public Order() {
		super();
	}
	
	public Order(Client client, OrderRequest order) {
		this();
		this.client = client;
		setCaseDate(order.getCaseDate());
		setCaseId(order.getCaseId());
		setCustomerUcn(order.getCustomerUcn());
		setUserId(order.getUserId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(client);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		Order other = (Order) obj;
		return Objects.equals(client, other.client);
	}

}
