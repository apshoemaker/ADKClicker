/**
 * 
 */
package com.adk.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author Paul Shoemaker
 *
 */
public interface GenericDAO<T, ID extends Serializable> {
	T findById(ID id, boolean lock);
	
	List<T> findAll();
    List<T> findByExample(T exampleInstance, 
                          String... excludeProperty);
    T makePersistent(T entity);
    void delete(T entity);
    Serializable save(T entity);
    void update(T entity);
    void closeSession();
    void flush();
    void clear();
    boolean open();
}
