package net.zoostar.wms.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Source extends AbstractStringPersistable {

	@Column
	private String sourceCode;
	
	@Column
	private String baseUrl;

	@Override
	public int hashCode() {
		return Objects.hash(sourceCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Source)) {
			return false;
		}
		Source other = (Source) obj;
		return Objects.equals(sourceCode, other.sourceCode);
	}
	
}
