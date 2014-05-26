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
package org.tinygroup.bizservice;

import java.util.Collection;

import org.tinygroup.dao.query.PagingObject;
import org.tinygroup.dao.query.QueryObject;
import org.tinygroup.logic.LogicInterface;

public class AbstractBizService<T, KeyType> implements
		BizServiceInterface<T, KeyType, QueryObject> {
	private LogicInterface<T, KeyType, QueryObject> logic;

	public LogicInterface<T, KeyType, QueryObject> getLogic() {
		return logic;
	}

	public void setLogic(LogicInterface<T, KeyType, QueryObject> logic) {
		this.logic = logic;
	}

	public T save(T object) {
		return logic.save(object);
	}

	public T update(T object) {
		return logic.update(object);
	}

	public T delete(T object) {
		return logic.delete(object);
	}

	public Collection<T> save(Collection<T> objects) {
		return logic.save(objects);
	}

	public Collection<T> update(Collection<T> objects) {
		return logic.update(objects);
	}

	public Collection<T> delete(Collection<T> objects) {
		return logic.delete(objects);
	}

	public T[] save(T[] objects) {
		return logic.save(objects);
	}

	public T[] update(T[] objects) {
		return logic.update(objects);
	}

	public T[] delete(T[] objects) {
		return logic.delete(objects);
	}

	public T get(KeyType key) {
		return logic.get(key);
	}

	public Collection<T> get(Collection<KeyType> key) {
		return logic.get(key);
	}

	public T[] get(KeyType[] key) {
		return logic.get(key);
	}

	public Collection<T> query(QueryObject queryObject) {
		return logic.query(queryObject);
	}

	public PagingObject<T> pagingQuery(QueryObject queryObject, int start,
			int limit) {
		return logic.pagingQuery(queryObject, start, limit);
	}

	public T getObject(String sql) {
		return logic.getObject(sql);
	}

	public Collection<T> query() {
		return logic.query();
	}

	public KeyType deleteByKey(KeyType key) {
		return logic.deleteByKey(key);
	}

	public KeyType[] deleteByKey(KeyType[] key) {
		return logic.deleteByKey(key);
	}

	public Collection<KeyType> deleteByKey(Collection<KeyType> key) {
		return logic.deleteByKey(key);
	}
}
