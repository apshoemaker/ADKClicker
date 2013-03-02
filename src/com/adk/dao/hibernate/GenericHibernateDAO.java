/**
 * 
 */
package com.adk.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.Transaction;

import java.lang.reflect.*;

import com.adk.dao.GenericDAO;
import com.adk.persistence.HibernateUtil;


/**
 * @author Paul Shoemaker
 *
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
		implements GenericDAO<T, ID> {
	private Class<T> persistentClass;
    private Session session;
    
    public GenericHibernateDAO() {
        this.persistentClass = (Class<T>)
           ( (ParameterizedType) getClass().getGenericSuperclass() )
                .getActualTypeArguments()[0];
    }
    
    public void setSession(Session s) {
        this.session = s;
    }
    
    protected Session getSession() {
        if (this.session == null)
            this.session = HibernateUtil.getSessionFactory().openSession();
        
        return this.session;
    }
    
    public Class<T> getPersistentClass() {
        return persistentClass;
    }
    
    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock)
            entity = (T) getSession()
                .load(getPersistentClass(), id, LockMode.UPGRADE);
        else
            entity = (T) getSession()
                .load(getPersistentClass(), id);
        
        return entity;
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return findByCriteria();
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance,
                                 String... excludeProperty) {
        Criteria crit =
            getSession().createCriteria(getPersistentClass());
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
    
    @SuppressWarnings("unchecked")
    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }
    
    public Serializable save(T entity){
    	Transaction tx = getSession().beginTransaction();
    	Serializable id = getSession().save(entity);
    	
    	tx.commit();
    	return id;
    }
    
    public void update(T entity){
    	Transaction tx = getSession().beginTransaction();

    	try{
    		getSession().update(entity);
    		tx.commit();
    	} catch (HibernateException e){
    		tx.rollback();
    	}
    }
    
    public void delete(T entity) {
    	Transaction tx = getSession().beginTransaction();
    	
        try{
        	getSession().delete(entity);
        	tx.commit();
        } catch (HibernateException e){
        	tx.rollback();
        }
    }
    
    public void flush() {
        getSession().flush();
    }
    
    public void clear() {
        getSession().clear();
    }
    
    public boolean open(){
    	return getSession().isOpen();
    }
    
    public void closeSession(){
    	this.session = null;
    	getSession().close();
    }
    
    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {

        Criteria crit =
            getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
       
    }
}
