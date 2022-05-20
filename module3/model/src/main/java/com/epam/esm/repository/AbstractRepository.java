package com.epam.esm.repository;

import com.epam.esm.repository.builder.PageSettings;
import com.epam.esm.repository.builder.QueryBuilder;
import org.hibernate.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

/**
 * AbstractDao
 *
 * @author alex
 * @version 1.0
 * @since 19.04.22
 */
@Repository
public abstract class AbstractRepository<T> {

    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> tClass;

    protected AbstractRepository(Class<T> tClass) {
        this.tClass = tClass;
    }

    protected abstract QueryBuilder<T> getQueryBuilder();

    public Optional<T> findById(long id) {
        return Optional.ofNullable(em.find(tClass, id));
    }

    public List<T> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage() - 1, pagination.getPageSize());
        return em.createQuery("SELECT e FROM " + tClass.getSimpleName() + " e", tClass)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Transactional
    public T create(T entity) {
        Session session = em.unwrap(Session.class);
        session.save(entity);
        return entity;
    }

    @Transactional
    public T update(T entity) {
        return em.merge(entity);
    }

    @Transactional
    public void delete(long id) {
        T entity = em.find(tClass, id);
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    public List<T> query(PageSettings pageSettings, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage() - 1, pagination.getPageSize());

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = getQueryBuilder().selectQuery(pageSettings, criteriaBuilder);

        return em.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
