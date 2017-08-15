package com.ews88.pay.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.ews88.pay.common.dao.BaseDao;
import com.ews88.pay.common.util.HqlQueryHelper;

public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T>{

	private Class<?> clazz;
	
	public BaseDaoImpl() {
		clazz = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Resource(name = "sessionFactory")
	public void resetsessionFactory(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public void persist(T entity) {
		getHibernateTemplate().persist(entity);
	}

	@Override
	public void delete(Serializable id) {
		getHibernateTemplate().delete(get(id));
	}

	@Override
	public void update(T entity) {
		getHibernateTemplate().update(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Serializable id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> selectWithHqlQueryHelper(final HqlQueryHelper helper) {
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(helper.getListQueryHql());
				if(helper.getParameters() != null && helper.getParameters().size() > 0) {
					int size = helper.getParameters().size();
					for (int i = 0; i < size; i++) {
						query.setParameter(i, helper.getParameters().get(i));
					}
				}
				return query.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getWithHqlQueryHelper(final HqlQueryHelper helper) {
		return getHibernateTemplate().execute(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(helper.getListQueryHql());
				if(helper.getParameters() != null && helper.getParameters().size() > 0) {
					int size = helper.getParameters().size();
					for (int i = 0; i < size; i++) {
						query.setParameter(i, helper.getParameters().get(i));
					}
				}
				return (T) query.uniqueResult();
			}
		});
	}

}
