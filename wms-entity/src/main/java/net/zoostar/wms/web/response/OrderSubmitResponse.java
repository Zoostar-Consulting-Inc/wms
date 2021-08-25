package net.zoostar.wms.web.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.wms.model.Order;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitResponse {

	private Order order;
	
	private HttpStatus status;
	
}
