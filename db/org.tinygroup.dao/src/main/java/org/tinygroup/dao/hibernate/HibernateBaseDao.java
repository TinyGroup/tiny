/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.tinygroup.dao.query.PagingObject;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class HibernateBaseDao extends HibernateDaoSupport {

	// 这里的返回值，可以直接返回getHibernateTemplate().save(entity)的值，这个会返回新插入记录的键值，也可以如下方式，返回一个持久化到数据库的对象
	public Object save(Object entity) throws Exception {
		getHibernateTemplate().save(entity);
		return entity;
	}

	public Object update(Object entity) throws Exception {
		getHibernateTemplate().update(entity);
		return entity;
	}

	// delete返回的是一个空值，因为记录都删除了，就不会有键值或对象的返回
	public void delete(Object entity) throws Exception {
		getHibernateTemplate().delete(entity);
	}

	public List getObjectList(String queryString, List<Object> params) {
		if (params.size() == 0)
			return getHibernateTemplate().find(queryString);
		Object[] array = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			array[i] = params.get(i);
		}
		return getHibernateTemplate().find(queryString, array);
	}

	public PagingObject getObjectList(final String queryString,
			final List<Object> params, final int start, final int limit) {
		try {
			PagingObject paging = new PagingObject();
			List list = getHibernateTemplate().executeFind(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createQuery(queryString);
							for (int i = 0; i < params.size(); i++) {
								query.setParameter(i, params.get(i));
							}
							List list2 = query.setFirstResult(start)
									.setMaxResults(limit).list();
							return list2;
						}
					});
			List total = getHibernateTemplate().executeFind(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createQuery(queryString);
							for (int i = 0; i < params.size(); i++) {
								query.setParameter(i, params.get(i));
							}
							List list2 = query.list();
							return list2;
						}
					});
			paging.setLimit(limit);
			paging.setStart(start);
			paging.setList(list);
			paging.setTotal(total.size());
			return paging;
		} catch (RuntimeException re) {

			throw re;
		}
	}

	public <T> T getObject(Class object, Object id) {
		return (T) getHibernateTemplate().get(object.getName(),
				(Serializable) id);
	}
	
	public Object deleteObject(Class clazz, Object id) throws Exception {
		Object t = getHibernateTemplate().load(clazz, (Serializable) id);
		delete(t);
		return id;
	}

	public Object getObject(String sql) {
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		if (list != null && list.size() >= 1)
			return list.get(0);
		return null;
	}
}