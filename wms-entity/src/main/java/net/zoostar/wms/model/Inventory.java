package net.zoostar.wms.model;

import java.util.Objects;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Inventory extends AbstractMultiSourceStringPersistable implements Comparable<Inventory> {

	private String assetId;
	
	private String sku;
	
	private String homeUcn;
	
	private String currentUcn;
	
	private int quantity;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(assetId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		Inventory other = (Inventory) obj;
		return Objects.equals(assetId, other.assetId);
	}

	@Override
	public int compareTo(Inventory that) {
		return this.assetId.compareTo(that.assetId);
	}

}