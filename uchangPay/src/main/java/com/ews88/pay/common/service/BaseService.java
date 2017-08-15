package com.ews88.pay.common.service;

import java.io.Serializable;
import java.util.List;

import com.ews88.pay.common.util.HqlQueryHelper;

public interface BaseService<T> {
	public void persist(T object);
	public void delete(Serializable id);
	public void update(T object);
	public T get(Serializable id);
	public List<T> findWithHqlQueryHelper(HqlQueryHelper helper);
	public T getWithHqlQueryHelper(HqlQueryHelper helper);
}
