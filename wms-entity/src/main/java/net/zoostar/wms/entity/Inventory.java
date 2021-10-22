package net.zoostar.wms.entity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Inventory extends AbstractMultiSourceStringPersistable {

	private String assetId;
	
	private String sku;
	
	private String homeUcn;
	
	private String currentUcn;
	
	private int quantity;

}