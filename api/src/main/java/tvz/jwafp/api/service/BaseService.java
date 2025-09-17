package tvz.jwafp.api.service;

import tvz.jwafp.api.entity.BaseEntity;
import tvz.jwafp.api.repo.BaseRepository;

public abstract class BaseService<T extends BaseEntity> {
    protected final BaseRepository<T> repository;

    protected BaseService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    public T create(T entity) {
        repository.create(entity);
        return entity;
    }

    public T update(T entity) {
        repository.update(entity);
        return entity;
    }

    public T getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public int deleteById(String id) {
        return repository.deleteById(id);
    }
}
