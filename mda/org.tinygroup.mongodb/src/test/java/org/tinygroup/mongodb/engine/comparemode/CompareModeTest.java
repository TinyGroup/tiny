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
package org.tinygroup.mongodb.engine.comparemode;

import junit.framework.TestCase;

import org.tinygroup.mongodb.DBClient;
import org.tinygroup.mongodb.db.MongodbPersistence;
import org.tinygroup.mongodb.engine.comparemode.impl.BeginsWithCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.ContainsWithCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.EndsWithCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.EqualsCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.GreaterOrEqualsCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.GreaterThanCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.IsEmptyCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.IsNullCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.LessOrEqualsCompareMode;
import org.tinygroup.mongodb.engine.comparemode.impl.LessThanCompareMode;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class CompareModeTest extends TestCase {

	private String collectionname = "test1"; // 测试数据存储的集合名称
	private DBCollection collection = null; // 数据库连接信息在文件"/mongodb.config"中配置
	MongodbPersistence persistence = null;

	protected void setUp() throws Exception {
		super.setUp();
		collection = DBClient.getCollection(collectionname); // 获取集合
		collection.remove(new BasicDBObject());// 清空该集合

		// 以下为插入测试数据
		BasicDBObject book = new BasicDBObject();
		book.put("name", "JAVA核心技术");
		book.put("price", 10.0);
		collection.insert(book);

		book = new BasicDBObject();
		book.put("name", "C++指南");
		book.put("price", 10.0);
		collection.insert(book);

		book = new BasicDBObject();
		book.put("name", "Mongodb手册");
		book.put("price", 20.0);
		collection.insert(book);

		book = new BasicDBObject();
		book.put("name", "spring入门");
		book.put("price", 20.0);
		collection.insert(book);

		book = new BasicDBObject();
		book.put("name", "web开发详解");
		book.put("price", 30.0);
		collection.insert(book);

		book = new BasicDBObject();
		book.put("name", "");
		book.put("price", 35.0);
		book.put("press", "武汉大学出版社");
		collection.insert(book);

		book = new BasicDBObject();
		book.put("name", null);
		book.put("price", 45.0);
		book.put("press", "武汉大学出版社");
		collection.insert(book);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	// 等于
	public void testEqualsCompareMode() {
		// 查询价钱等于20的图书
		MongoCompareMode compareMode = new EqualsCompareMode();
		DBObject ref = (DBObject) compareMode.generateBSONObject("price", 20, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			assertTrue((Double) dbObject.get("price") == 20);
		}
	}

	// 判断字段值是否为空字符串(null或空字符串)
	public void testIsEmptyCompareMode() {
		// 查询name为空的图书
		MongoCompareMode compareMode = new IsEmptyCompareMode();
		BasicDBObject ref = (BasicDBObject) compareMode
				.generateBSONObject("name", null, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			assertTrue(((String) dbObject.get("name")).equals(""));
		}
	}

	public void testBeginsWithCompareMode() {
		// 查询书名以"开发"开头的图书
		MongoCompareMode compareMode = new BeginsWithCompareMode();
		DBObject ref  = (DBObject) compareMode.generateBSONObject("name", "开发", null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			String name = (String) dbObject.get("name");
			assertTrue(name.indexOf("开发") == 0);
		}
	}

	public void testContainsWithCompareMode() {
		// 查书名包含includeStr 变量值的图书
		String includeStr = "开123发";
		MongoCompareMode compareMode = new ContainsWithCompareMode();
		DBObject ref = (DBObject) compareMode.generateBSONObject("name", includeStr, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			assertTrue(((String) dbObject.get("name")).indexOf(includeStr) != -1);
		}
	}

	public void testEndsWithCompareMode() {
		// 查询书名以"手册"结尾的图书
		MongoCompareMode compareMode = new EndsWithCompareMode();
		DBObject ref = (DBObject) compareMode.generateBSONObject("name", "手册", null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			String name = (String) dbObject.get("name");
			assertTrue(name.indexOf("手册") == name.length() - 2);
		}
	}

	public void testGreaterOrEqualsCompareMode() {
		// 查询价钱大于等于20的图书
		MongoCompareMode compareMode = new GreaterOrEqualsCompareMode();
		DBObject ref = (DBObject) compareMode.generateBSONObject("price", 20, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			assertTrue((Double) dbObject.get("price") >= 20);
		}
	}

	public void testGreaterThanCompareMode() {
		// 查询价钱大于20的图书
		MongoCompareMode compareMode = new GreaterThanCompareMode();
		DBObject ref = (DBObject) compareMode.generateBSONObject("price", 20, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			assertTrue((Double) dbObject.get("price") > 20);
		}
	}

	public void testIsNullCompareMode() {
		// 查询价钱为null的的图书
		MongoCompareMode compareMode = new IsNullCompareMode();
		BasicDBObject ref = (BasicDBObject) compareMode
				.generateBSONObject("price",null, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
		}
	}

	public void testLessOrEqualsCompareMode() {
		// 查询价钱小于等于20的图书
		MongoCompareMode compareMode = new LessOrEqualsCompareMode();
		DBObject ref = (DBObject) compareMode.generateBSONObject("price", 20, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			assertTrue((Double) dbObject.get("price") <= 20);
		}
	}

	public void testLessThanCompareMode() {
		// 查询价钱小于20的图书
		MongoCompareMode compareMode = new LessThanCompareMode();
		DBObject ref  = (DBObject) compareMode.generateBSONObject("price", 20, null);

		// 执行查询，并输出结果
		DBCursor dbCursor = collection.find(ref);
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			System.out.println(dbObject.toString());
			assertTrue((Double) dbObject.get("price") < 20);
		}
	}

}
