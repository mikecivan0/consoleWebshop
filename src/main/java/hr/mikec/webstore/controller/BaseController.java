package hr.mikec.webstore.controller;


import java.util.List;

import hr.mikec.webstore.util.BaseException;
import hr.mikec.webstore.util.HibernateUtil;
import org.hibernate.Session;

public abstract class BaseController<T> {

    protected Session session;
    protected T entity;

    public abstract List<T> read();
    protected abstract void createControl() throws BaseException;
    protected abstract void updateControl() throws BaseException;
    protected abstract void deleteControl() throws BaseException;

    public BaseController() throws BaseException {
        this.session = HibernateUtil.getSession();
    }

    public T create() throws BaseException{
        createControl();
        save();
        return entity;
    }

    public T update() throws BaseException{
        updateControl();
        save();
        return entity;
    }

    public void delete() throws BaseException{
        deleteControl();
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
    }

    private void save() {
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Session getSession() {
        return session;
    }



}

