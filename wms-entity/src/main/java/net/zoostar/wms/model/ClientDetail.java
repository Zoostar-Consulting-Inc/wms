package net.zoostar.wms.model;

import java.util.Objects;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientDetail extends AbstractStringPersistable implements Comparable<ClientDetail> {
  	
	@ManyToOne(fetch = FetchType.EAGER)
    private Client client;
    
    private String ucn;
    
    private String name;

	@Override
	public int hashCode() {
		return Objects.hash(ucn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ClientDetail)) {
			return false;
		}
		ClientDetail other = (ClientDetail) obj;
		return Objects.equals(ucn, other.ucn);
	}

	@Override
	public int compareTo(ClientDetail that) {
		return this.client.compareTo(that.getClient()) == 0 ? this.ucn.compareTo(that.getUcn()) :
				this.client.compareTo(that.getClient());
	}
}
