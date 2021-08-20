package net.zoostar.wms.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Client extends AbstractStringPersistable implements Comparable<Client> {
	
	private String code;
	
	private String name;
	
	private String baseUrl;

	@OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
	private Set<ClientDetail> details;
	
	@Override
	public int hashCode() {
		return Objects.hash(code);
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
		return Objects.equals(code, other.code);
	}

	@Override
	public int compareTo(Client that) {
		return this.code.compareTo(that.getCode());
	}
	
}
