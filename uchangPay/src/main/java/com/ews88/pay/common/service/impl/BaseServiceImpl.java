package com.ews88.pay.common.service.impl;

import java.io.Serializable;
import java.util.List;

import com.ews88.pay.common.dao.BaseDao;
import com.ews88.pay.common.service.BaseService;
import com.ews88.pay.common.util.HqlQueryHelper;

public class BaseServiceImpl<T> implements BaseService<T>{
	
	private BaseDao<T> basedao;

	public void setBasedao(BaseDao<T> basedao) {
		this.basedao = basedao;
	}

	@Override
	public void persist(T object) {
		basedao.persist(object);
	}

	@Override
	public void delete(Serializable id) {
		basedao.delete(id);
	}

	@Override
	public void update(T object) {
		basedao.update(object);
	}

	@Override
	public T get(Serializable id) {
		return basedao.get(id);
	}

	@Override
	public List<T> findWithHqlQueryHelper(HqlQueryHelper helper) {
		return basedao.selectWithHqlQueryHelper(helper);
	}

	@Override
	public T getWithHqlQueryHelper(HqlQueryHelper helper) {
		return basedao.getWithHqlQueryHelper(helper);
	}

}
