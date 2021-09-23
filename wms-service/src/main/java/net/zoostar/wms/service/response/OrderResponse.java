
package net.zoostar.wms.service.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.wms.web.request.OrderRequest;

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
		OrderRequest order = response.getBody();
		setAssetIds(order.getAssetIds());
		setCaseDate(order.getCaseDate());
		setCaseId(order.getCaseId());
		setCustomerUcn(order.getCustomerUcn());
		setUserId(order.getUserId());
		this.clientCode = clientCode;
		this.status = response.getStatusCode();
	}
}
