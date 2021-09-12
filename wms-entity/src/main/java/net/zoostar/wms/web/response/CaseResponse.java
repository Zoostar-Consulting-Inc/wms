package net.zoostar.wms.web.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.wms.model.AbstractCase;
import net.zoostar.wms.web.request.Case;

@Getter
@Setter
@ToString
public class CaseResponse extends AbstractCase {

	private Map<String, HttpStatus> responses;

	protected CaseResponse() {
		super();
	}
	
	public CaseResponse(Case order) {
		this();
		setCaseId(order.getCaseId());
		setCaseDate(order.getCaseDate());
		setCustomerUcn(order.getCustomerUcn());
		setUserId(order.getUserId());
		this.responses = new HashMap<>(order.getAssetIds().size());
		order.getAssetIds().stream().forEach(entity -> responses.put(entity, HttpStatus.OK));
	}

}
