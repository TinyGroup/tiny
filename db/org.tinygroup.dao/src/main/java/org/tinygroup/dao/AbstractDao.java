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
package org.tinygroup.dao;

import org.tinygroup.dao.hibernate.HibernateBaseDaoImpl;
import org.tinygroup.dao.query.PagingObject;
import org.tinygroup.dao.query.QueryObject;
import org.tinygroup.dao.util.DaoUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 抽象Dao
 * 
 * @author luoguo
 * 
 * @param <T>
 */
public abstract class AbstractDao<T, KeyType> implements
		DaoInterface<T, KeyType, QueryObject> {
	private Logger logger = LoggerFactory.getLogger(AbstractDao.class);
	private Class<T> entityClass;

	public AbstractDao() {
		entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Resource
	HibernateBaseDaoImpl basicDaoService;// basicDaoService对应BasicDaoImpl成为数据访问组件的bean名字

	protected HibernateBaseDaoImpl getBaseDaoImpl() {
		return basicDaoService;
	}

	public T save(T object) {
		try {
			return (T) basicDaoService.save(object);
		} catch (Exception e) {
			logger.errorMessage("保存对象失败",e);
		}
		return null;
	}

	public T update(T object) {
		try {
			return (T) basicDaoService.update(object);
		} catch (Exception e) {

			logger.errorMessage("更新对象失败",e);
		}
		return null;
	}

	public T delete(T object) {

		try {
			basicDaoService.delete(object);
		} catch (Exception e) {
			logger.errorMessage("删除对象失败",e);
		}

		return object;
	}

	public Collection<T> save(Collection<T> objects) {
		try {
			return (Collection<T>) basicDaoService.save(objects);
		} catch (Exception e) {
			logger.errorMessage("保存对象集合失败",e);
		}
		return null;
	}

	public Collection<T> delete(Collection<T> objects) {
		try {
			basicDaoService.delete(objects);
		} catch (Exception e) {
			logger.errorMessage("删除对象集合失败",e);
		}
		return null;
	}

	public T[] save(T[] objects) {
		try {
			return (T[]) basicDaoService.save(objects);
		} catch (Exception e) {
			logger.errorMessage("保存对象数组失败",e);
		}
		return null;
	}

	public T[] delete(T[] objects) {
		try {
			return (T[]) basicDaoService.delete(objects);
		} catch (Exception e) {
			
			logger.errorMessage("删除对象数组失败",e);
		}
		return null;
	}

	public Collection<T> update(Collection<T> objects) {
		try {
			return (Collection<T>) basicDaoService.update(objects);
		} catch (Exception e) {
			logger.errorMessage("更新对象集合失败",e);
		}
		return null;
	}

	public T[] update(T[] objects) {
		try {
			return (T[]) basicDaoService.update(objects);
		} catch (Exception e) {
			logger.errorMessage("更新对象数组失败",e);
		}
		return null;
	}

	public Collection<T> query(QueryObject queryObject) {
		queryObject = checkNull(queryObject);
		List<Object> params = new ArrayList<Object>();
		String queryString = DaoUtil.QueryObjectToHql(queryObject, params);
		List list = basicDaoService.getObjectList(queryString, params);
		return list;
	}
	
	public Collection<T> query() {
		return query(null);
	}

	public PagingObject<T> pagingQuery(QueryObject queryObject, int start, int limit) {
		queryObject = checkNull(queryObject);
		List<Object> params = new ArrayList<Object>();
		String queryString = DaoUtil.QueryObjectToHql(queryObject, params);
		return basicDaoService.getObjectList(queryString, params, start,
				limit);
	}

	public T get(KeyType key) {
		return (T)basicDaoService.getObject(entityClass, key);
	}

	public Collection<T> get(Collection<KeyType> key) {
		
		return (Collection<T>)basicDaoService.getObjects(entityClass, key);
	}

	public T[] get(KeyType[] key) {
		
		return (T[])basicDaoService.getObjects(entityClass, key);
	}

	public T getObject(String sql) {
		return (T) basicDaoService.getObject(sql);
	}
	
	

	public KeyType deleteByKey(KeyType key) {
		try {
			basicDaoService.deleteObject(entityClass, key);
		} catch (Exception e) {
			logger.errorMessage("根据key删除对象失败",e);
		}
		return key;
	}

	public Collection<KeyType> deleteByKey(Collection<KeyType> keys) {
		
		try {
			basicDaoService.deleteObject(entityClass, keys);
		} catch (Exception e) {
			logger.errorMessage("根据key集合删除对象失败",e);
		}
		return keys;
	}

	public KeyType[] deleteByKey(KeyType[] keys) {
		try {
			 basicDaoService.deleteObject(entityClass, keys);
		} catch (Exception e) {
			logger.errorMessage("根据key数组删除对象失败",e);
		}
		return keys;
	}

	private QueryObject checkNull(QueryObject queryObject){
		if(queryObject == null ){
			QueryObject tqueryObject = new QueryObject();
			tqueryObject.setEntityName(DaoUtil.getEntityName(entityClass));
			return tqueryObject;
		}
		if(queryObject.getEntityName()==null ||"".equals(queryObject.getEntityName())){
			queryObject.setEntityName(DaoUtil.getEntityName(entityClass));
		}
		return queryObject;
	}
}
