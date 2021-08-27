package net.zoostar.wms.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Client implements Comparable<Client>, Persistable<String> {

	@Id
	@Column(name = "client_id", length = 50)
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	private String id;

	private String code;
	
	private String name;
	
	private String baseUrl;

	@OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
	private Set<ClientDetail> details;

	@JsonIgnore
	@Override
	public boolean isNew() {
		return StringUtils.isBlank(id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}
		
		Client that = (Client) obj;
		return Objects.equals(code, that.code);
	}

	@Override
	public int compareTo(Client that) {
		return this.code.compareTo(that.getCode());
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Client [id=").append(id).append(", code=").append(code).append(", name=").append(name)
				.append(", baseUrl=").append(baseUrl).append(", details=")
				.append(details != null ? details.size() : 0).append("]");
		return builder.toString();
	}

}
