package net.zoostar.wms.web.request;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.wms.model.Case;
import net.zoostar.wms.model.EntityWrapper;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CaseOrderSubmitRequest implements EntityWrapper<Case> {
	
	private String caseId;
	
	private long caseDate;

	private String customerUcn;
	
	private String userId;
	
	private Set<String> assetIds;

	public CaseOrderSubmitRequest(Case order) {
		this.caseId = order.getCaseId();
		this.caseDate = order.getCaseDate();
		this.customerUcn = order.getCustomerUcn();
		this.userId = order.getUserId();
		this.assetIds = order.getAssetIds();
	}

	@Override
	public Case toEntity() {
		Case order = new Case();
		order.setAssetIds(assetIds);
		order.setCaseDate(caseDate);
		order.setCaseId(caseId);
		order.setCustomerUcn(customerUcn);
		order.setUserId(userId);
		return order;
	}
	
}
