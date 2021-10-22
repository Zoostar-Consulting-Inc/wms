package net.zoostar.wms.entity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Customer extends AbstractMultiSourceStringPersistable {

	private String email;
	
	private String name;
	
	private String locationId;

}
