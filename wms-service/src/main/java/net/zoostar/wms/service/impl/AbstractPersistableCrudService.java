package net.zoostar.wms.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.service.PersistableCrudService;

@Slf4j
@Getter
@Setter
@Service
@Transactional
public abstract class AbstractPersistableCrudService<T extends Persistable<ID>, ID>
implements PersistableCrudService<T, ID> {

	@Override
	public T create(T persistable) {
		log.info("Persisting {}...", persistable.toString());
		if(persistable.isNew() && findByKey(persistable) == null) {
			return getRepository().save(persistable);
		}
		throw new EntityExistsException(String.format("Entity exists: %s", persistable.toString()));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> retrieve(int page, int size) {
		log.info("Retrieving {} {} for page {}...", size, getClazz().getCanonicalName(), page);
		Pageable pageable = PageRequest.of(page, size);
		return getRepository().findAll(pageable);
	}

	@Override
	public T update(T persistable) {
		if(persistable.isNew() || getRepository().findById(persistable.getId()) == null) {
			throw new NoSuchElementException(String.format(
					"No entity found to update for: %s", persistable.toString()));
		} else {
			return getRepository().save(persistable);
		}
	}

	@Override
	public T delete(ID id) {
		Optional<T> entity = getRepository().findById(id);
		if(entity.isEmpty()) {
			throw new NoSuchElementException(String.format(
					"No entity found to delete by id: %s", id.toString()));
		} else {
			getRepository().deleteById(id);
		}
		return entity.get();
	}

	public abstract PagingAndSortingRepository<T, ID> getRepository();
	protected abstract Class<T> getClazz();
	protected abstract T findByKey(T persistable);

}
