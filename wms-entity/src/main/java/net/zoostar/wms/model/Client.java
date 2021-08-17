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
public class Client extends AbstractStringPersistable implements Comparable<Client> {
	
	private String ucn;
	
	private String name;
	
	private String baseUrl;

	@Override
	public int hashCode() {
		return Objects.hash(ucn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Client)) {
			return false;
		}
		Client other = (Client) obj;
		return Objects.equals(ucn, other.ucn);
	}

	@Override
	public int compareTo(Client that) {
		return this.ucn.compareTo(that.getUcn());
	}
	
}
