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
package org.tinygroup.logic;

import java.util.Collection;

import org.tinygroup.dao.DaoInterface;
import org.tinygroup.dao.query.PagingObject;
import org.tinygroup.dao.query.QueryObject;

public  class AbstractLogic<T, KeyType> implements LogicInterface<T, KeyType, QueryObject>{
	private DaoInterface<T, KeyType, QueryObject> dao;
	
	public DaoInterface<T, KeyType, QueryObject> getDao() {
		return dao;
	}

	public void setDao(DaoInterface<T, KeyType, QueryObject> dao) {
		this.dao = dao;
	}

	public T save(T object) {
		return dao.save(object);
	}

	public T update(T object) {
		return dao.update(object);
	}

	public T delete(T object) {
		return dao.delete(object);
	}

	public Collection<T> save(Collection<T> objects) {
		return dao.save(objects);
	}

	public Collection<T> update(Collection<T> objects) {
		return dao.update(objects);
	}

	public Collection<T> delete(Collection<T> objects) {
		return dao.delete(objects);
	}

	public T[] save(T[] objects) {
		return dao.save(objects);
	}

	public T[] update(T[] objects) {
		return dao.update(objects);
	}

	public T[] delete(T[] objects) {
		return dao.delete(objects);
	}

	public T get(KeyType key) {
		return dao.get(key);
	}

	public Collection<T> get(Collection<KeyType> key) {
		return dao.get(key);
	}

	public T[] get(KeyType[] key) {
		return dao.get(key);
	}

	public Collection<T> query(QueryObject queryObject) {
		return dao.query(queryObject);
	}

	public PagingObject<T> pagingQuery(QueryObject queryObject, int start,
			int limit) {
		return dao.pagingQuery(queryObject, start, limit);
	}

	public T getObject(String sql) {
		return dao.getObject(sql);
	}

	public Collection<T> query() {
		return dao.query();
	}

	public KeyType deleteByKey(KeyType key) {
		return dao.deleteByKey(key);
	}

	public KeyType[] deleteByKey(KeyType[] key) {
		return dao.deleteByKey(key);
	}

	public Collection<KeyType> deleteByKey(Collection<KeyType> key) {
		return dao.deleteByKey(key);
	}
}
