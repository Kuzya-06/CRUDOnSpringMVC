package ru.kuznetsov.controller.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kuznetsov.domain.Task;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TaskDAO {
    private final SessionFactory sessionFactory;

    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Task> getAll(int offset, int limit) {
        Query<Task> query = getSession().createQuery("select t from Task t", Task.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public int getAllCount() {
        Query<Long> query = getSession().createQuery("select count(t) from Task t", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    @Transactional
    public Task getById(int id) {
        Query<Task> query = getSession().createQuery("select t from Task t where t.id = :ID", Task.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }

    @Transactional
    public void saveOrUpdate(Task task) {
        getSession().persist(task);
    }

    @Transactional
    public void delete(Task task) {
        getSession().remove(task);
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}