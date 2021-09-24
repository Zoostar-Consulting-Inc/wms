package net.zoostar.wms.api.outbound;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.wms.api.inbound.OrderRequest;

@Getter
@Setter
@ToString(callSuper = true)
public class OrderResponse extends OrderRequest {

	private String clientCode;
	
	private HttpStatus status;
	
	public OrderResponse() {
		super();
	}
	
	public OrderResponse(ResponseEntity<OrderRequest> response, String clientCode) {
		this();
		this.clientCode = clientCode;
		if(response != null) {
			this.status = response.getStatusCode();
			OrderRequest order = response.getBody();
			if(order != null) {
				setAssetIds(order.getAssetIds());
				setCaseDate(order.getCaseDate());
				setCaseId(order.getCaseId());
				setCustomerUcn(order.getCustomerUcn());
				setUserId(order.getUserId());
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(clientCode);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		OrderResponse other = (OrderResponse) obj;
		return Objects.equals(clientCode, other.clientCode);
	}
}
