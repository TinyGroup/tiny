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
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongodbDataGenerator {

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");

	public static List<DBObject> testCommon() {
		List<DBObject> list = new ArrayList<DBObject>();

		DBObject book = new BasicDBObject();
		book.put("_id", 1);
		book.put("name", "java开发");
		book.put("price", 10.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 2);
		book.put("name", "C++开发");
		book.put("price", 20.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 3);
		book.put("name", "oracle开发");
		book.put("price", 30.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 4);
		book.put("name", "sybase开发");
		book.put("price", 40.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 5);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 6);
		book.put("name", "mongodb开发");
		book.put("price", 60.0);
		book.put("press", "清华大学出版社");
		list.add(book);

		return list;
	}

	public static List<DBObject> testGroup() {
		List<DBObject> list = new ArrayList<DBObject>();

		DBObject book = new BasicDBObject();
		book.put("_id", 1);
		book.put("name", "java开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 2);
		book.put("name", "C++开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 3);
		book.put("name", "oracle开发");
		book.put("price", 30.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 4);
		book.put("name", "sybase开发");
		book.put("price", 58.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 5);
		book.put("name", "mongodb开发");
		book.put("price", 58.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 7);
		book.put("name", "mongodb开发");
		book.put("price", 30.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 8);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 9);
		book.put("name", "mongodb开发");
		book.put("price", 10.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 10);
		book.put("name", "mongodb开发");
		book.put("price", 5.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 12);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "清华大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 14);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "清华大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 15);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "清华大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 16);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "复旦大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 17);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "复旦大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 6);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "南京大学出版社");
		list.add(book);

		return list;
	}

	public static List<DBObject> testW() {
		List<DBObject> list = new ArrayList<DBObject>();

		DBObject book = new BasicDBObject();
		book.put("_id", 1);
		book.put("name", "java开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 2);
		book.put("name", "C++开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 3);
		book.put("name", "oracle开发");
		book.put("price", 30.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 4);
		book.put("name", "sybase开发");
		book.put("price", 58.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 5);
		book.put("name", "java开发");
		book.put("price", 58.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 6);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "清华大学出版社");
		list.add(book);

		return list;
	}

	public static List<DBObject> testSort() {
		List<DBObject> list = new ArrayList<DBObject>();

		DBObject book = new BasicDBObject();
		book.put("_id", 1);
		book.put("name", "java开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 2);
		book.put("name", "C++开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 3);
		book.put("name", "oracle开发");
		book.put("price", 30.0);
		book.put("press", "浙江大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 4);
		book.put("name", "sybase开发");
		book.put("price", 58.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 5);
		book.put("name", "java开发");
		book.put("price", 58.0);
		book.put("press", "北京大学出版社");
		list.add(book);

		book = new BasicDBObject();
		book.put("_id", 6);
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "清华大学出版社");
		list.add(book);

		return list;
	}

	/**
	 * 插入普通查询测试数据
	 * 
	 * @return
	 */
	public static List<DBObject> commonData() {
		List<DBObject> list = new ArrayList<DBObject>();

		// 以下为插入测试数据
		DBObject book = new BasicDBObject();
		book.put("name", "java开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		try {
			book.put("date", df.parse("2006-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 100);
		BasicDBObject address = new BasicDBObject();
		address.put("province", "浙江");
		address.put("city", "杭州");
		address.put("zipcode", "310012");
		book.put("address", address);
		book.put("author", new String[] { "乔峰", "虚竹", "段誉" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "C++开发");
		book.put("price", 50.0);
		book.put("press", "南京大学出版社");
		try {
			book.put("date", df.parse("2006-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 200);
		address = new BasicDBObject();
		address.put("province", "");
		address.put("city", "南京");
		address.put("zipcode", "234567");
		book.put("address", address);
		book.put("author", new String[] { "张三", "令狐冲", "王五" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "oracle开发");
		book.put("price", 30.0);
		book.put("press", "清华大学出版社");
		try {
			book.put("date", df.parse("2006-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 300);
		address = new BasicDBObject();
		address.put("province", "北京");
		address.put("city", "北京");
		address.put("zipcode", "010010");
		book.put("address", address);
		book.put("author", new String[] { "张三", "李四", "小龙女" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "sybase开发");
		book.put("price", 58.0);
		book.put("press", "北京大学出版社");
		try {
			book.put("date", df.parse("2011-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 400);
		address = new BasicDBObject();
		address.put("province", "北京");
		address.put("city", "北京");
		address.put("zipcode", "010010");
		book.put("address", address);
		book.put("author", new String[] { "阿飞", "李四", "王五" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "武汉大学出版社");
		try {
			book.put("date", df.parse("2011-02-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 150);
		address = new BasicDBObject();
		address.put("province", "湖北");
		address.put("city", "武汉");
		address.put("zipcode", "123456");
		book.put("address", address);
		book.put("author", new String[] { "张三", "李四" });
		list.add(book);

		return list;

	}

	/**
	 * 测试数据
	 * 
	 * @return
	 */
	public static List<DBObject> genTestData() {
		List<DBObject> list = new ArrayList<DBObject>();

		// 以下为插入测试数据
		DBObject book = new BasicDBObject();
		book.put("name", "java开发");
		book.put("price", 50.0);
		book.put("press", "浙江大学出版社");
		try {
			book.put("date", df.parse("2006-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 100);
		BasicDBObject address = new BasicDBObject();
		address.put("province", "浙江");
		address.put("city", "杭州");
		address.put("zipcode", "310012");
		book.put("address", address);
		book.put("author", new String[] { "乔峰", "虚竹", "段誉" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "C++开发");
		book.put("price", 50.0);
		book.put("press", "南京大学出版社");
		try {
			book.put("date", df.parse("2006-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 200);
		address = new BasicDBObject();
		address.put("province", "");
		address.put("city", "南京");
		address.put("zipcode", "234567");
		book.put("address", address);
		book.put("author", new String[] { "张三", "令狐冲", "王五" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "oracle开发");
		book.put("price", 30.0);
		book.put("press", "清华大学出版社");
		try {
			book.put("date", df.parse("2006-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 300);
		address = new BasicDBObject();
		address.put("province", "北京");
		address.put("city", "北京");
		address.put("zipcode", "010010");
		book.put("address", address);
		book.put("author", new String[] { "张三", "李四", "小龙女" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "sybase开发");
		book.put("price", 58.0);
		book.put("press", "北京大学出版社");
		try {
			book.put("date", df.parse("2011-12-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 400);
		address = new BasicDBObject();
		address.put("province", "北京");
		address.put("city", "北京");
		address.put("zipcode", "010010");
		book.put("address", address);
		book.put("author", new String[] { "阿飞", "李四", "王五" });
		list.add(book);

		book = new BasicDBObject();
		book.put("name", "mongodb开发");
		book.put("price", 50.0);
		book.put("press", "武汉大学出版社");
		try {
			book.put("date", df.parse("2011-02-13"));
		} catch (ParseException e) {
		}
		book.put("pages", 150);
		address = new BasicDBObject();
		address.put("province", "湖北");
		address.put("city", "武汉");
		address.put("zipcode", "123456");
		book.put("address", address);
		book.put("author", new String[] { "张三", "李四" });
		list.add(book);

		return list;
	}

}
