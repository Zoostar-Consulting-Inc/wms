package net.zoostar.wms.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbstractMultiSourceStringPersistable extends AbstractStringPersistable
		implements MultiSourceEntity {
	
	@Column(length = 10)
	private String sourceCode;

	@Column(length = 64)
	private String sourceId;

	@Override
	public int hashCode() {
		return Objects.hash(sourceCode, sourceId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractMultiSourceStringPersistable)) {
			return false;
		}
		AbstractMultiSourceStringPersistable other = (AbstractMultiSourceStringPersistable) obj;
		return Objects.equals(sourceCode, other.sourceCode) && Objects.equals(sourceId, other.sourceId);
	}

}
