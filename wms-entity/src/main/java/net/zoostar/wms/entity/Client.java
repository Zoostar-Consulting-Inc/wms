package net.zoostar.wms.entity;

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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client implements Comparable<Client>, Persistable<String> {

	@Id
	@Column(name = "client_id", length = 50)
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	private String id;

	@EqualsAndHashCode.Include
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
