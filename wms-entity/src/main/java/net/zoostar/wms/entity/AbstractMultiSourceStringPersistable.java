package net.zoostar.wms.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractMultiSourceStringPersistable extends AbstractStringPersistable
		implements MultiSourceEntity {

	@Override
	public int hashCode() {
		return Objects.hash(source);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractMultiSourceStringPersistable)) {
			return false;
		}
		AbstractMultiSourceStringPersistable other = (AbstractMultiSourceStringPersistable) obj;
		return Objects.equals(source, other.source);
	}
	
	@Column(length = 10)
	private String source;
}
