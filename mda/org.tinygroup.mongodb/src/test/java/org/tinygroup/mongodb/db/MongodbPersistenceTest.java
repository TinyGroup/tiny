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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.bson.BSONObject;
import org.tinygroup.mongodb.DBClient;
import org.tinygroup.mongodb.engine.PageInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import junit.framework.TestCase;

public class MongodbPersistenceTest extends TestCase {

	private String collectionname = "test1"; // 测试数据存储的集合名称
	private DBCollection collection = null; // 数据库连接信息在文件"/mongodb.config"中配置
	MongodbPersistence persistence = null;
	private String separatorline = "-----------------------------------------------------------------------------------------------------------------------";

	/*************************************************************************************************************************************/

	protected void setUp() throws Exception {
		super.setUp();
		collection = DBClient.getCollection(collectionname); // 获取集合
		persistence = new MongodbPersistence(collectionname);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private void insertTestData(List<DBObject> dbObjects) {
		collection.insert(dbObjects);
		System.out.println("在集合" + collectionname + "中，插入了如下测试数据：");
		for (DBObject dbObject : dbObjects) {
			System.out.println(dbObject);
		}
		System.out.println("-----------------------------------------------");
	}

	private void cleanCollection() {
		collection.remove(new BasicDBObject());//
		System.out.println(collection.getName() + "集合数据清空完成！");
	}

	private void printResultInfo(BSONObject[] result) {
		System.out.println(separatorline);
		if (result == null || result.length == 0) {
			System.out.println("结果为空");
			return;

		}

		StringBuilder _sb = new StringBuilder();
		_sb.append("记录数: " + result.length);
		System.out.println(_sb.toString());

		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i].toString());
		}
	}

	private void printResultInfo(PageInfo pageInfo) {
		System.out.println(separatorline);
		if (pageInfo == null) {
			System.out.println("结果为空");
			return;
		}
		BSONObject[] objects = pageInfo.getObjects();

		StringBuilder _sb = new StringBuilder();
		_sb.append("totalSize=" + pageInfo.getTotalSize());
		_sb.append("; totalPages=" + pageInfo.getTotalPages());
		_sb.append("; pageSize=" + pageInfo.getPageSize());
		_sb.append(" pageNumber=" + pageInfo.getPageNumber());
		System.out.println(_sb.toString());

		if (objects == null || objects.length == 0) {
			System.out.println("结果为空");
		} else {
			for (int i = 0; i < objects.length; i++) {
				System.out.println(objects[i].toString());
			}
		}
	}

	private void printCollection() {
		DBCursor dbCursor = collection.find();
		while (dbCursor.hasNext()) {
			System.out.println(dbCursor.next());
		}
	}

	/*************************************** 普通查询,没有分组 *************************************************************************/

	/**
	 * select t.column1, t.column2 form tablename t
	 */
	public void testCommon() {
		cleanCollection(); // 清空集合数据
		insertTestData(MongodbDataGenerator.testCommon()); // 插入测试数据

		DBObject select = null; // select子句
		BasicDBObject where = null; // where子句
		DBObject sort = null; // sort子句

		select = new BasicDBObject();
		select.put("出版社", 1);

		sort = new BasicDBObject("price", -1);
		sort.put("pages", 1);

		/******************************************************************************************************************/
		where = new BasicDBObject();
		where.put("name", "啊啊啊啊啊");
		PageInfo pageInfo = new MongodbPersistence(collectionname).find(null,
				where, null, null, null, 1, 100);
		BSONObject[] bsonObjects = persistence.find(null, where);
		assertNotNull(pageInfo);
		assertEquals(0, pageInfo.getTotalSize());
		assertEquals(0, pageInfo.getTotalPages());
		assertEquals(100, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(0, bsonObjects.length);

		/******************************************************************************************************************/
		where = new BasicDBObject();
		where.put("name", "啊啊啊啊啊");
		pageInfo = new MongodbPersistence(collectionname).find(null, where,
				null, null, null, 1, 3);
		bsonObjects = persistence.find(null, where);
		assertNotNull(pageInfo);
		assertEquals(0, pageInfo.getTotalSize());
		assertEquals(0, pageInfo.getTotalPages());
		assertEquals(3, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(0, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = new MongodbPersistence(collectionname).find(null, null,
				null, null, null, 1, 2);
		bsonObjects = persistence.find(null, null);
		assertEquals(6, pageInfo.getTotalSize());
		assertEquals(3, pageInfo.getTotalPages());
		assertEquals(2, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(6, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = new MongodbPersistence(collectionname).find(null, null,
				null, null, null, 1, 100);
		bsonObjects = persistence.find(null, null);
		assertEquals(6, pageInfo.getTotalSize());
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(100, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(6, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = new MongodbPersistence(collectionname).find(null, null,
				null, null, null, 12, 15);
		bsonObjects = persistence.find(null, null);
		assertEquals(6, pageInfo.getTotalSize());
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(15, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(6, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = new MongodbPersistence(collectionname).find(null, null,
				null, null, null, 4, 3);
		bsonObjects = persistence.find(null, null);
		assertEquals(6, pageInfo.getTotalSize());
		assertEquals(2, pageInfo.getTotalPages());
		assertEquals(3, pageInfo.getPageSize());
		assertEquals(2, pageInfo.getPageNumber());
		assertEquals(6, bsonObjects.length);

		/******************************************************************************************************************/
		where = new BasicDBObject("price", new BasicDBObject("$gte", 40));
		sort = new BasicDBObject("price", -1);
		pageInfo = new MongodbPersistence(collectionname).find(null, where,
				null, null, sort, 4, 2);
		bsonObjects = persistence.find(null, where);
		assertEquals(3, pageInfo.getTotalSize());
		assertEquals(2, pageInfo.getTotalPages());
		assertEquals(2, pageInfo.getPageSize());
		assertEquals(2, pageInfo.getPageNumber());
		assertEquals(3, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = new MongodbPersistence(collectionname).find(null, null,
				null, null, null, 1, 4);
		bsonObjects = persistence.find(null, null);
		assertEquals(6, pageInfo.getTotalSize());
		assertEquals(2, pageInfo.getTotalPages());
		assertEquals(4, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(6, bsonObjects.length);
		BSONObject[] objects = pageInfo.getObjects();
		assertEquals(4, objects.length);

		System.out.println("testCommon测试完成");
	}

	/*************************************** 分组查询测试 *************************************************************************/
	public void testGroup() {
		cleanCollection(); // 清空集合数据
		insertTestData(MongodbDataGenerator.testGroup()); // 插入测试数据

		// DBObject select = new BasicDBObject("出版社", 1); // select子句
		DBObject sort = new BasicDBObject("总数", -1); // sort子句

		// where子句
		BasicDBObject where = new BasicDBObject("price", new BasicDBObject(
				"$gte", 20));

		// group子句
		DBObject _group = new BasicDBObject("出版社", "$press");
		DBObject group = new BasicDBObject("_id", _group);
		group.put("总数", new BasicDBObject("$sum", 1)); // 总数

		// having子句
		BasicDBObject having = new BasicDBObject("总数", new BasicDBObject(
				"$gte", 2));

		/******************************************************************************************************************/
		PageInfo pageInfo = persistence.find(null, null, group, null, null, 1,
				100);
		BSONObject[] bsonObjects = persistence.find(null, null, group, null,
				null);
		printResultInfo(pageInfo);
		assertNotNull(pageInfo);
		assertEquals(5, pageInfo.getTotalSize());
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(100, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(5, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = persistence.find(null, null, group, null, null, 1, 2);
		bsonObjects = persistence.find(null, null, group, null, null);
		printResultInfo(pageInfo);
		//		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(5, pageInfo.getTotalSize());
		assertEquals(3, pageInfo.getTotalPages());
		assertEquals(2, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(5, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = persistence.find(null, null, group, null, null, 1, 14);
		bsonObjects = persistence.find(null, null, group, null, null);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(5, pageInfo.getTotalSize());
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(14, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(5, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = persistence.find(null, null, group, null, null, 14, 14);
		bsonObjects = persistence.find(null, null, group, null, null);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(5, pageInfo.getTotalSize());
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(14, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(5, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = persistence.find(null, null, group, null, null, 1, 2);
		bsonObjects = persistence.find(null, null, group, null, null);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(5, pageInfo.getTotalSize());
		assertEquals(3, pageInfo.getTotalPages());
		assertEquals(2, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(5, bsonObjects.length);

		/******************************************************************************************************************/
		pageInfo = persistence.find(null, null, group, null, null, 4, 2);
		bsonObjects = persistence.find(null, null, group, null, null);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(5, pageInfo.getTotalSize());
		assertEquals(3, pageInfo.getTotalPages());
		assertEquals(2, pageInfo.getPageSize());
		assertEquals(3, pageInfo.getPageNumber());
		assertEquals(5, bsonObjects.length);

		/******************************************************************************************************************/
		where = new BasicDBObject("price", new BasicDBObject("$gte", 1000));
		pageInfo = persistence.find(null, where, group, null, null, 4, 14);
		bsonObjects = persistence.find(null, where, group, null, null);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(0, pageInfo.getTotalSize());
		assertEquals(0, pageInfo.getTotalPages());
		assertEquals(14, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(0, bsonObjects.length);

		/******************************************************************************************************************/
		sort = new BasicDBObject("总数", -1);
		pageInfo = persistence.find(null, null, group, null, sort, 1, 2);
		bsonObjects = persistence.find(null, null, group, null, sort);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(5, pageInfo.getTotalSize());
		assertEquals(3, pageInfo.getTotalPages());
		assertEquals(2, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(5, bsonObjects.length);

		/******************************************************************************************************************/
		having = new BasicDBObject("总数", new BasicDBObject("$gte", 3));
		sort = new BasicDBObject("总数", 1);
		pageInfo = persistence.find(null, null, group, having, sort, 4, 14);
		bsonObjects = persistence.find(null, null, group, having, sort);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);
		assertEquals(3, pageInfo.getTotalSize());
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(14, pageInfo.getPageSize());
		assertEquals(1, pageInfo.getPageNumber());
		assertEquals(3, bsonObjects.length);

		/******************************************************************************************************************/
		bsonObjects = persistence.find(null, null, null, having, sort);
		printResultInfo(pageInfo);
		printResultInfo(bsonObjects);
		assertNotNull(pageInfo);

		System.out.println("testGroup测试完成");
	}

	/*************************************************************************************************************************************/

	public void testInsert() {
		cleanCollection(); // 清空集合数据

		// 以下为插入测试数据
		BasicDBObject book = new BasicDBObject();
		book.put("name", "java开发");
		book.put("price", 10.0);
		book.put("press", "浙江大学出版社");
		try {
			book.put("date",
					new SimpleDateFormat("yyyy-mm-dd").parse("2006-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 100);
		BasicDBObject address = new BasicDBObject();
		address.put("province", "浙江");
		address.put("city", "杭州");
		address.put("zipcode", "310012");
		book.put("address", address);
		book.put("author", new String[] { "乔峰", "虚竹", "段誉" });

		new MongodbPersistence("insertTest").insert(book);

		System.out.println("testInsert测试完成");
	}

	public void testDelete() {
		cleanCollection(); // 清空集合数据
		insertTestData(MongodbDataGenerator.testCommon()); // 插入测试数据

		BasicDBObject book = new BasicDBObject();
		book.put("name", "java开发");

		new MongodbPersistence("deleteTest").delete(book);

		System.out.println("testDelete测试完成");
	}

	public void testUpdate() {
		cleanCollection(); // 清空集合数据
		insertTestData(MongodbDataGenerator.testCommon()); // 插入测试数据

		BasicDBObject src = new BasicDBObject();
		System.out.println("更新前数据");
		System.out.println(separatorline);
		printCollection();

		BasicDBObject target = new BasicDBObject();
		target.put("name", "恒生电子");
		long result = persistence.update(src, target);
		assertEquals(6, result);

		System.out.println("更新后数据");
		System.out.println(separatorline);
		printCollection();

		System.out.println("testUpdate测试完成");
	}

	/**
	 * 添加字段测试
	 */
	public void testAddColumn() {
		cleanCollection(); // 清空集合数据

		// 新字段
		BasicDBObject newColumn = new BasicDBObject("title", null);

		// 执行查询，并输出结果
		persistence.addColumn(newColumn);

		System.out.println("testAddColumn测试完成");
	}

	/**
	 * 删除字段测试
	 */
	public void testRemoveColumn() {
		cleanCollection(); // 清空集合数据
		insertTestData(MongodbDataGenerator.commonData()); // 插入测试数据

		// 需要删除的字段字段
		BasicDBObject removeColumn = new BasicDBObject("price", null);

		persistence.removeColumn(removeColumn);

		System.out.println("testRemoveColumn测试完成");
	}

}
