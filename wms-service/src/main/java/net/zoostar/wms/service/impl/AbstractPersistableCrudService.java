package net.zoostar.wms.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.entity.AbstractStringPersistable;
import net.zoostar.wms.service.StringPersistableCrudService;

@Slf4j
@Getter
@Setter
@Service
@Transactional
public abstract class AbstractPersistableCrudService<T extends AbstractStringPersistable>
implements StringPersistableCrudService<T> {

	@Override
	public T create(T persistable) {
		log.info("Persisting {}...", persistable.toString());
		if(persistable.isNew() && retrieveByKey(persistable) == null) {
			persistable.setActive(true);
			persistable.setUpdate(System.currentTimeMillis());
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
		if(persistable.isNew() || getRepository().findById(persistable.getId()).isEmpty()) {
			throw new NoSuchElementException(String.format(
					"No entity found to update for: %s", persistable.toString()));
		} else {
			persistable.setUpdate(System.currentTimeMillis());
			return getRepository().save(persistable);
		}
	}

	@Override
	public T delete(String id) {
		T entity = null;
		Optional<T> optional = getRepository().findById(id);
		if(optional.isEmpty()) {
			throw new NoSuchElementException(String.format(
					"No entity found to delete by id: %s", id.toString()));
		} else {
			entity = optional.get();
			entity.setActive(false);
			entity.setUpdate(System.currentTimeMillis());
			entity = getRepository().save(entity);
		}
		return entity;
	}

	public abstract PagingAndSortingRepository<T, String> getRepository();
	protected abstract Class<T> getClazz();

}
