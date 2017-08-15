package com.ews88.pay.common.dao;

import java.io.Serializable;
import java.util.List;

import com.ews88.pay.common.util.HqlQueryHelper;

public interface BaseDao<T> {
	public void persist(T entity);
	public void delete(Serializable id);
	public void update(T entity);
	public T get(Serializable id);
	public List<T> selectWithHqlQueryHelper(HqlQueryHelper helper);
	public T getWithHqlQueryHelper(HqlQueryHelper helper);
}
