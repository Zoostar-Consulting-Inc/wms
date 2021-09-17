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
public class Customer extends AbstractMultiSourceStringPersistable implements Comparable<Customer> {

	private String email;
	
	private String name;
	
	private String locationId;
	
	@Override
	public int compareTo(Customer that) {
		return this.locationId.compareTo(that.getLocationId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(locationId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		Customer other = (Customer) obj;
		return Objects.equals(locationId, other.locationId);
	}

}
