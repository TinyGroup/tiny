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
package org.tinygroup.mongodb.db;

import java.util.List;

import org.bson.BSONObject;
import org.tinygroup.mongodb.DBClient;
import org.tinygroup.mongodb.engine.PageInfo;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongodbPersistence {

	private DBCollection collection;

	public MongodbPersistence(String collectionName) {
		collection = DBClient.getCollection(collectionName);
	}

	/**
	 * 
	 * 普通查询，没有分组，分页，排序
	 * 
	 * @param select
	 * @param where
	 * @return
	 */
	public BSONObject[] find(DBObject select, DBObject where) {
		DBObject keys = select == null ? new BasicDBObject() : select; // 选择列对象，类似关系数据库中select子句
		DBObject ref = where == null ? new BasicDBObject() : where; // 过滤条件对象，类似关系数据库中where子句

		int totalSize = (new Long(collection.count(ref))).intValue(); // 查询结果总记录数
		DBCursor dbCursor = collection.find(ref, keys); // 查询结果游标操作

		// 将游标组装成数组
		BSONObject[] bsonObjects = new BasicDBObject[totalSize];
		int i = 0;
		while (dbCursor.hasNext()) {
			bsonObjects[i++] = dbCursor.next();
		}

		return bsonObjects;
	}

	/**
	 * 
	 没有分页，groupby为null时，进行分组查询，否则不分组
	 * 
	 * @param select
	 * @param where
	 * @param group
	 * @param having
	 * @param sort
	 * @return
	 */
	public BSONObject[] find(DBObject select, DBObject where, DBObject group,
			DBObject having, DBObject sort) {
		if (group == null) {
			return find(select, where);
		} else {
			BasicDBList dbList = aggregate(select, where, group, having, sort);
			if (dbList != null && dbList.size() > 0) {
				BSONObject[] objects = new BSONObject[dbList.size()];
				objects = new BSONObject[dbList.size()];
				for (int i = 0; i < dbList.size(); i++) {
					BSONObject bson = (BSONObject) dbList.get(i);
					BSONObject _id = (BSONObject) bson.get("_id");
					bson.removeField("_id");
					bson.putAll(_id);
					objects[i] = bson;
				}
				return objects;
			} else {
				return new BSONObject[0];
			}
		}
	}

	/**
	 * 
	 分页查询，groupby为null时，进行分组查询，否则不分组
	 * 
	 * @param select
	 * @param where
	 * @param groupby
	 * @param having
	 * @param sort
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo find(DBObject select, DBObject where, DBObject groupby,
			DBObject having, DBObject sort, int pageNumber, int pageSize) {
		if (groupby == null) {
			return find(select, where, sort, pageNumber, pageSize);
		} else {
			return aggregate(select, where, groupby, having, sort, pageNumber,
					pageSize);
		}
	}

	/**
	 * 插入记录
	 * 
	 * @param dbObject
	 * @return
	 */
	public BSONObject insert(DBObject dbObject) {
		collection.insert(dbObject);
		// WriteResult result = collection.insert(dbObject);
		// String id = (String) result.getField("_id");

		DBCursor dbCursor = collection.find(dbObject);
		if (dbCursor.hasNext()) {
			return dbCursor.next();

		}

		return null;
	}

	/**
	 * 
	 * 新增对象数组记录
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public BSONObject insertObjectArrayModel(DBObject src, DBObject target) {
		target = new BasicDBObject("$addToSet", target);
		collection.update(src, target, true, true);
		DBCursor dbCursor = collection.find(src);
		if (dbCursor.hasNext()) {
			return dbCursor.next();
		}
		return null;
	}

	/**
	 * 
	 * 删除对象数组记录
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public BSONObject removeObjectModel(DBObject src, DBObject target) {
		collection.update(src, target, true, true);
		DBCursor dbCursor = collection.find(src);
		if (dbCursor.hasNext()) {
			return dbCursor.next();
		}
		return null;
	}

	/**
	 * 
	 * 批量插入记录
	 * 
	 * @param dbObjects
	 */
	public void insert(List<DBObject> dbObjects) {
		collection.insert(dbObjects);
	}

	/**
	 * 
	 * 删除记录
	 * 
	 * @param dbObject
	 * @return
	 */
	public long delete(DBObject dbObject) {
		long count = collection.count(dbObject);
		collection.remove(dbObject);
		return count;
	}

	/**
	 * 
	 * 更新记录
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public long update(DBObject src, DBObject target) {
		target = new BasicDBObject("$set", target);
		long count = collection.count(src);
		collection.update(src, target, true, true);
		return count;
	}

	/**
	 * 所有记录添加字段
	 * 
	 * @param newColumn
	 *            需要添加的字段
	 */
	public void addColumn(BasicDBObject newColumn) {
		BasicDBObject update = new BasicDBObject("$set", newColumn);
		DBObject ref = new BasicDBObject();
		collection.update(ref, update, true, true);
	}

	/**
	 * 删除所有的指定字段
	 * 
	 * @param 需要删除的字段
	 */
	public void removeColumn(BasicDBObject removeColumn) {
		BasicDBObject update = new BasicDBObject("$unset", removeColumn);
		DBObject ref = new BasicDBObject();
		collection.update(ref, update, true, true);
	}

	/******************************************************************************************************************************/

	/**
	 * 获取符合条件的分页范围内的查询结果封装对象，不分组
	 * 
	 * @param select
	 * @param where
	 * @param group
	 * @param having
	 * @param sort
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	private PageInfo find(DBObject select, DBObject where, DBObject sort,
			int pageNum, int pageSize) {
		DBObject keys = select != null ? select : new BasicDBObject(); // 选择列对象，类似关系数据库中select子句
		DBObject ref = where != null ? where : new BasicDBObject(); // 过滤条件对象，类似关系数据库中where子句
		DBCursor dbCursor = null; // 查询结果集游标
		int totalSize = (new Long(collection.count(ref))).intValue(); // 总记录数
		PageInfo pageInfo = new PageInfo(); // 分页对象
		pageInfo.pageAttributeSet(pageSize, pageNum, totalSize);
		if (sort == null) {
			dbCursor = collection.find(ref, keys).skip(pageInfo.getStart())
					.limit(pageInfo.getPageSize());
		} else {
			dbCursor = collection.find(ref, keys).sort(sort)
					.skip(pageInfo.getStart()).limit(pageInfo.getPageSize());
		}

		int length = pageInfo.getArraySize();
		BSONObject[] objects = new BSONObject[length];
		pageInfo.setObjects(objects);
		fillObjects(dbCursor, objects); // 将结果集放入PageInfo对象
		return pageInfo;
	}

	/**
	 * 获取符合条件的分页范围内的分组查询结果封装对象
	 * 
	 * @param select
	 * @param where
	 * @param group
	 * @param having
	 * @param sort
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	private PageInfo aggregate(DBObject select, DBObject where, DBObject group,
			DBObject having, DBObject sort, int pageNum, int pageSize) {
		int totalSize = count(select, where, group, having, sort); // 结果集总数
		PageInfo pageInfo = new PageInfo(); // 分页对象
		pageInfo.pageAttributeSet(pageSize, pageNum, totalSize);
		BasicDBList dbList = aggregateRange(select, where, group, having, sort,
				pageInfo.getPageNumber(), pageInfo.getPageSize());// 该分页结果集
		BSONObject[] objects = new BSONObject[dbList.size()];
		fillObjects(dbList, objects); // 将结果集放入PageInfo对象
		return pageInfo;
	}

	/**
	 * 
	 获取所有符合条件的分组查询结果列表，没有分页
	 * 
	 * @param select
	 * @param where
	 * @param group
	 * @param having
	 * @param sort
	 * @return
	 */
	private BasicDBList aggregate(DBObject select, DBObject where,
			DBObject group, DBObject having, DBObject sort) {
		DBObject groupDBObject = new BasicDBObject("$group", group);
		DBObject selectDBObject = select != null ? new BasicDBObject(
				"$project", select) : null;
		DBObject whereDBObject = where != null ? new BasicDBObject("$match",
				where) : new BasicDBObject("$match", new BasicDBObject());
		DBObject havingDBObject = having != null ? new BasicDBObject("$match",
				having) : new BasicDBObject("$match", new BasicDBObject());
		DBObject sortDBObject = sort != null ? new BasicDBObject("$sort", sort)
				: null;

		if (selectDBObject == null) {
			if (sortDBObject == null) {
				AggregationOutput output = collection.aggregate(whereDBObject,
						groupDBObject, havingDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			} else {
				AggregationOutput output = collection.aggregate(whereDBObject,
						groupDBObject, havingDBObject, sortDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			}
		} else {
			if (sortDBObject == null) {
				AggregationOutput output = collection.aggregate(selectDBObject,
						whereDBObject, groupDBObject, havingDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			} else {
				AggregationOutput output = collection.aggregate(selectDBObject,
						whereDBObject, groupDBObject, havingDBObject,
						sortDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			}
		}
	}

	/**
	 * 
	 获取符合条件的分页范围内的分组查询结果列表
	 * 
	 * @param select
	 * @param where
	 * @param group
	 * @param having
	 * @param sort
	 * @return
	 */
	private BasicDBList aggregateRange(DBObject select, DBObject where,
			DBObject group, DBObject having, DBObject sort, int skip, int limit) {
		DBObject groupDBObject = new BasicDBObject("$group", group); // group必须存在
		DBObject selectDBObject = select != null ? new BasicDBObject(
				"$project", select) : null;
		DBObject whereDBObject = where != null ? new BasicDBObject("$match",
				where) : new BasicDBObject("$match", new BasicDBObject());
		DBObject havingDBObject = having != null ? new BasicDBObject("$match",
				having) : new BasicDBObject("$match", new BasicDBObject());
		DBObject sortDBObject = sort != null ? new BasicDBObject("$sort", sort)
				: null;
		DBObject skipDBObject = new BasicDBObject("$skip", skip);
		DBObject limitDBObject = new BasicDBObject("$limit", limit);

		if (selectDBObject == null) {
			if (sortDBObject == null) {
				AggregationOutput output = collection.aggregate(whereDBObject,
						groupDBObject, havingDBObject, skipDBObject,
						limitDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			} else {
				AggregationOutput output = collection.aggregate(whereDBObject,
						groupDBObject, havingDBObject, sortDBObject,
						skipDBObject, limitDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			}
		} else {
			if (sortDBObject == null) {
				AggregationOutput output = collection.aggregate(selectDBObject,
						whereDBObject, groupDBObject, havingDBObject,
						skipDBObject, limitDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			} else {
				AggregationOutput output = collection.aggregate(selectDBObject,
						whereDBObject, groupDBObject, havingDBObject,
						sortDBObject, skipDBObject, limitDBObject);
				return (BasicDBList) output.getCommandResult().get("result");
			}
		}

	}

	/**
	 * 
	 获取所有符合条件的分组查询结果记录总数
	 * 
	 * @param select
	 * @param where
	 * @param group
	 * @param having
	 * @param sort
	 * @return
	 */
	private int count(DBObject select, DBObject where, DBObject group,
			DBObject having, DBObject sort) {
		BasicDBList result = aggregate(select, where, group, having, sort); // 结果集总数
		return result.size();
	}

	/**
	 * 将游标引用的对象复制到数组中
	 * 
	 * @param dbCursor
	 *            游标
	 * @param objects
	 *            目标数组
	 */
	private void fillObjects(DBCursor source, BSONObject[] tageter) {
		int length = tageter.length;
		for (int i = 0; i < length; i++) {
			if (source.hasNext()) {
				tageter[i] = (BSONObject) source.next();
			}
		}
	}

	/**
	 * 将列表中的对象复制到数组中
	 * 
	 * @param dbList
	 *            源列表
	 * @param objects
	 *            目标数组
	 */
	private void fillObjects(BasicDBList source, BSONObject[] tageter) {
		if (source != null && source.size() > 0) {
			tageter = new BSONObject[source.size()];
			for (int i = 0; i < source.size(); i++) {
				BSONObject bson = (BSONObject) source.get(i);
				BSONObject _id = (BSONObject) bson.get("_id");
				bson.removeField("_id");
				bson.putAll(_id);
				tageter[i] = bson;
			}
		}
	}

}
