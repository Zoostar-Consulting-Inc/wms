package net.zoostar.wms.model;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends AbstractMultiSourceStringPersistable implements Comparable<User> {

	private String userId;

	@Override
	public int compareTo(User that) {
		return this.userId.compareTo(that.getUserId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(userId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		User other = (User) obj;
		return Objects.equals(userId, other.userId);
	}

}
