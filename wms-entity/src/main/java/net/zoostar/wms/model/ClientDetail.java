package net.zoostar.wms.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ClientDetail extends AbstractStringPersistable implements Comparable<ClientDetail> {
  	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="client_id", nullable=false)
    private Client client;
    
    private String ucn;
    
    private String name;

	@Override
	public int hashCode() {
		return Objects.hash(client, ucn);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ClientDetail that = (ClientDetail) obj;
		return Objects.equals(client, that.client) && Objects.equals(ucn, that.ucn);
	}

	@Override
	public int compareTo(ClientDetail that) {
		return this.client.compareTo(that.getClient()) == 0 ? this.ucn.compareTo(that.getUcn()) :
				this.client.compareTo(that.getClient());
	}
}
