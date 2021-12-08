package net.zoostar.wms.entity;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(sku);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Inventory)) {
			return false;
		}
		Inventory other = (Inventory) obj;
		return Objects.equals(sku, other.sku);
	}

}