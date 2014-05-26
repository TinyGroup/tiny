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

import java.util.Collection;

import org.tinygroup.dao.query.PagingObject;

/**
 * Dao通用接口
 * 
 * @author luoguo
 * 
 * @param <T>
 */
public interface DaoInterface<T, KeyType, QueryObjectType> {

	T save(T object);

	T update(T object);

	T delete(T object);

	

	Collection<T> save(Collection<T> objects);

	Collection<T> update(Collection<T> objects);

	Collection<T> delete(Collection<T> objects);

	T[] save(T[] objects);

	T[] update(T[] objects);

	T[] delete(T[] objects);

	T get(KeyType key);

	Collection<T> get(Collection<KeyType> key);

	T[] get(KeyType[] key);

	Collection<T> query(QueryObjectType queryObject);
	
	PagingObject<T> pagingQuery(QueryObjectType queryObject, int start,
			int limit);

	T getObject(String sql);

	Collection<T> query();
	
	KeyType deleteByKey(KeyType key);
	
	KeyType[] deleteByKey(KeyType[] key);
	
	Collection<KeyType> deleteByKey(Collection<KeyType> key);
}