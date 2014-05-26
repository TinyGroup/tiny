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
package org.tinygroup.dbrouterjdbc3.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouterjdbc3.jdbc.util.FileUtil;

public class ShardModeSameSchemaTest extends TestCase {

	private static final String TINY_DRIVER = "org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver"; // tiny驱动
	private static final String DERBY_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver"; // derby驱动

	private static final String ROUTER_CONFIG = "/shardModeSameSchema.xml"; // tiny路由配置
	private static final String URL = "jdbc:dbrouter://shardModeSameSchema"; // url
	private static final String USER = "luog"; // 用户名
	private static final String PASSWORD = "123456"; // 密码

	private static final String DERBY_DBPATH = "derbydb"; // derby数据库存放目录
	private static final String DERBY_DB = "dbShard"; // derby数据库
	private static final String[] DERBY_TEACHER_TABLES = { "teacher" }; // 教师表
	private static final String[] DERBY_STUDENT_TABLES = { "student", // 学生表
			"student0", "student1", "student2" }; // derby数据库存放目录

	private static RouterManager routerManager; // tiny路由管理器
	private static boolean inited; // 是否已初始化

	protected void setUp() throws Exception {
		super.setUp();
		// init();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// destroy();
	}

	public static void main(String[] args) {
		Connection conn = null;
		try {
			init(); // 初始化derby数据库和表
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			ShardModeSameSchemaTest test = new ShardModeSameSchemaTest();
			test.relatedQueryTest(conn);
			test.statementTest(conn);
			test.resultSetTest(conn);
			test.relatedQueryTest2(conn);
		} catch (Exception e) {
			throw new RuntimeException("测试失败", e);
		} finally {
			close(conn, null, null);
			destroy(); // 关闭derby数据库，并清理对应文件夹
		}
	}

	public void test() {
		// Connection conn = null;
		// try {
		// conn = DriverManager.getConnection(URL, USER, PASSWORD);
		// relatedQueryTest(conn);
		// statementTest(conn);
		// resultSetTest(conn);
		// relatedQueryTest2(conn);
		// } catch (Exception e) {
		// destroy(); // 关闭derby数据库，并清理对应文件夹
		// throw new RuntimeException("测试失败", e);
		// } finally {
		// close(conn, null, null); // 关闭连接
		// }
	}

	/**
	 * 两张表关联查询测试
	 * 
	 * @param conn
	 * @throws Exception
	 */
	private void relatedQueryTest(Connection conn) throws Exception {
		Statement st = null;
		ResultSet rs = null;

		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);

		List<String> _sList = new ArrayList<String>();
		List<Integer> _iList = new ArrayList<Integer>();

		// addBatch操作
		st.executeUpdate("delete from teacher");
		st.executeUpdate("insert into teacher(id,name) values(1,'zhang')");
		st.executeUpdate("insert into teacher(id,name) values(2,'qian')");
		st.executeUpdate("insert into teacher(id,name) values(3,'sun')");
		st.executeUpdate("insert into teacher(id,name) values(4,'wang')");
		rs = st.executeQuery("select count(*) from teacher");
		if (rs.next()) {
			assertEquals(4, rs.getInt(1));
		}

		// 准备数据
		prepareRecord(st);
		String sql = "select * from student where name in (select name from student where id<5 and id>2) order by id desc";
		rs = st.executeQuery(sql);
		_sList.clear();
		while (rs.next()) {
			_sList.add(rs.getString("name"));
		}
		assertEquals(2, _sList.size());
		assertEquals("s4", _sList.get(0));
		assertEquals("s3", _sList.get(1));

		sql = "select sum(age) from student where tId in (select id from teacher where id<3) group by tId";
		rs = st.executeQuery(sql);
		_iList.clear();
		while (rs.next()) {
			_iList.add(rs.getInt(1));
		}
		assertEquals(2, _sList.size());
		assertEquals(23, _iList.get(0).intValue());
		assertEquals(27, _iList.get(1).intValue());

		sql = "SELECT age FROM (SELECT s.*, t.name AS tName FROM student s, teacher t WHERE t.id=s.tId AND t.id<3 AND s.id>1) aa GROUP BY age HAVING age >12 AND age<14";
		rs = st.executeQuery(sql);
		_iList.clear();
		while (rs.next()) {
			_iList.add(rs.getInt(1));
		}
		assertEquals(13, _iList.get(0).intValue());

		sql = "SELECT age FROM (SELECT s.*, t.name AS tName FROM student s JOIN teacher t ON t.id=s.tId AND t.id<3 AND s.id>1) aa GROUP BY age HAVING age >12 AND age<14";
		rs = st.executeQuery(sql);
		_iList.clear();
		while (rs.next()) {
			_iList.add(rs.getInt(1));
		}
		assertEquals(13, _iList.get(0).intValue());

		sql = "SELECT count(*) FROM student s LEFT JOIN teacher t ON t.id=s.tId WHERE s.id=7";
		rs = st.executeQuery(sql);
		if (rs.next()) {
			assertEquals(0, rs.getInt(1));
		}

		sql = "SELECT count(*) FROM student s RIGHT JOIN teacher t ON t.id=s.tId WHERE s.id=7";
		rs = st.executeQuery(sql);
		if (rs.next()) {
			assertEquals(0, rs.getInt(1));
		}

		sql = "SELECT count(*) FROM student s RIGHT JOIN teacher t ON t.id=s.tId WHERE t.id=4";
		rs = st.executeQuery(sql);
		if (rs.next()) {
			assertEquals(1, rs.getInt(1));
		}

		sql = "SELECT count(*) FROM student s RIGHT JOIN teacher t ON t.id=s.tId WHERE s.id=7";
		rs = st.executeQuery(sql);
		if (rs.next()) {
			assertEquals(0, rs.getInt(1));
		}

		close(null, st, rs);
		System.out.println("relatedQueryTest执行结束!");
	}

	/**
	 * Statement相关测试，比如clearWarnings，setMaxFieldSize，setMaxRows等
	 * 
	 * @param conn
	 * @throws Exception
	 */
	private void statementTest(Connection conn) throws Exception {
		ResultSet rs = null;
		Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_UPDATABLE, 102);

		st.addBatch("delete from teacher");
		st.addBatch("insert into teacher(id,name) values(1,'zhang')");
		st.addBatch("insert into teacher(id,name) values(2,'qian')");
		st.clearBatch();
		st.executeBatch();
		rs = st.executeQuery("select * from teacher");

		st.setFetchDirection(ResultSet.FETCH_REVERSE);
		st.setFetchSize(30);
		assertEquals(30, st.getFetchSize());

		st.setMaxFieldSize(101);
		st.setMaxRows(3);
		st.setQueryTimeout(13);
		assertEquals(3, st.getMaxRows());
		assertEquals(false, st.getMoreResults());
		assertEquals(false, st.getMoreResults(1));
		assertEquals(13, st.getQueryTimeout());

		st.getResultSet();

		assertEquals(ResultSet.CONCUR_UPDATABLE, st.getResultSetConcurrency());
		assertEquals(ResultSet.TYPE_FORWARD_ONLY, st.getResultSetType());
		st.getWarnings();

		close(null, st, rs);
		System.out.println("statementTest 执行结束");
	}

	/**
	 * ResultSet相关测试,比如first,last,absolute等
	 * 
	 * @param conn
	 * @throws Exception
	 */
	private void resultSetTest(Connection conn) throws Exception {
		ResultSet rs = null;
		Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		// 准备数据
		st.addBatch("delete from teacher");
		st.addBatch("insert into teacher(id,name) values(1,'zhang')");
		st.addBatch("insert into teacher(id,name) values(2,'qian')");
		st.addBatch("insert into teacher(id,name) values(3,'sun')");
		st.addBatch("insert into teacher(id,name) values(4,'wang')");
		st.executeBatch();
		rs = st.executeQuery("select * from teacher");
		rs.setFetchDirection(ResultSet.FETCH_REVERSE);
		rs.setFetchSize(101);

		rs.absolute(3);
		assertEquals("sun", rs.getString("name"));
		assertEquals(false, rs.isFirst());
		assertEquals(false, rs.isLast());
		assertEquals(false, rs.isBeforeFirst());
		assertEquals(false, rs.isAfterLast());

		rs.absolute(100);
		assertEquals(false, rs.absolute(100));
		assertEquals(false, rs.isFirst());
		assertEquals(false, rs.isLast());
		assertEquals(false, rs.isBeforeFirst());
		assertEquals(true, rs.isAfterLast());

		rs.absolute(-10);
		assertEquals(false, rs.absolute(-10));
		assertEquals(false, rs.isFirst());
		assertEquals(false, rs.isLast());
		assertEquals(true, rs.isBeforeFirst());
		assertEquals(false, rs.isAfterLast());

		rs.first();
		assertEquals("zhang", rs.getString("name"));
		assertEquals(true, rs.isFirst());
		assertEquals(false, rs.isLast());
		assertEquals(false, rs.isBeforeFirst());
		assertEquals(false, rs.isAfterLast());

		rs.last();
		assertEquals("wang", rs.getString("name"));
		assertEquals(false, rs.isFirst());
		assertEquals(true, rs.isLast());
		assertEquals(false, rs.isBeforeFirst());
		assertEquals(false, rs.isAfterLast());

		rs.beforeFirst();
		assertEquals(false, rs.isFirst());
		assertEquals(false, rs.isLast());
		assertEquals(true, rs.isBeforeFirst());
		assertEquals(false, rs.isAfterLast());

		rs.afterLast();
		assertEquals(false, rs.isFirst());
		assertEquals(false, rs.isLast());
		assertEquals(false, rs.isBeforeFirst());
		assertEquals(true, rs.isAfterLast());

		rs.beforeFirst();
		assertEquals(true, rs.next());
		assertEquals(false, rs.previous());
		assertEquals(false, rs.isFirst());
		assertEquals(false, rs.isLast());
		assertEquals(true, rs.isBeforeFirst());
		assertEquals(false, rs.isAfterLast());

		rs.afterLast();
		assertEquals(true, rs.previous());
		String firstName1 = rs.getString("name");
		rs.last();
		String firstName2 = rs.getString("name");
		assertEquals(true, firstName1.equals(firstName2));

		rs.beforeFirst();
		assertEquals(true, rs.next());
		firstName1 = rs.getString("name");
		rs.first();
		firstName2 = rs.getString("name");
		assertEquals(true, firstName1.equals(firstName2));

		rs.clearWarnings();
		assertEquals(null, rs.getWarnings());

		assertEquals(1, rs.findColumn("id"));
		assertEquals(2, rs.findColumn("name"));

		ResultSetMetaData metaDate = rs.getMetaData();
		assertEquals(2, metaDate.getColumnCount());
		assertEquals("ID", metaDate.getColumnName(1));

		close(null, st, rs);
		System.out.println("resultSetTest 执行结束");
	}

	/**
	 * 两张表关联测试
	 * 
	 * @param conn
	 * @throws Exception
	 */
	public void relatedQueryTest2(Connection conn) throws Exception {
		ResultSet rs = null;
		List<String> _sList = new ArrayList<String>();
		Statement st = conn.createStatement();

		String sql = "select * from student where id in (select id from student where id<3)";
		rs = st.executeQuery(sql);
		_sList.clear();
		while (rs.next()) {
			_sList.add(rs.getString("name"));
		}
		assertEquals(2, _sList.size());
		assertEquals("s1", _sList.get(0));
		assertEquals("s2", _sList.get(1));

		sql = "select * from student where tId=(select id from teacher where name='qian')";
		rs = st.executeQuery(sql);
		_sList.clear();
		while (rs.next()) {
			_sList.add(rs.getString("name"));
		}
		assertEquals(2, _sList.size());
		assertEquals("s3", _sList.get(0));
		assertEquals("s4", _sList.get(1));
		sql = "select s.id as sId, s.name as sName, s.age, t.id as tId, t.name as tName from student s, teacher t where t.id=s.tId";
		rs = st.executeQuery(sql);
		_sList.clear();
		while (rs.next()) {
			_sList.add(rs.getString("sName"));
		}
		assertEquals(6, _sList.size());
		assertEquals("s3", _sList.get(0));
		assertEquals("s6", _sList.get(1));
		assertEquals("s1", _sList.get(2));
		assertEquals("s4", _sList.get(3));
		assertEquals("s2", _sList.get(4));
		assertEquals("s5", _sList.get(5));

		sql = "select s.id as sId, s.name as sName, s.age, t.id as tId, t.name as tName from student s join teacher t on t.id=s.tId";
		rs = st.executeQuery(sql);
		_sList.clear();
		while (rs.next()) {
			_sList.add(rs.getString("sName"));
		}
		assertEquals(6, _sList.size());
		assertEquals("s3", _sList.get(0));
		assertEquals("s6", _sList.get(1));
		assertEquals("s1", _sList.get(2));
		assertEquals("s4", _sList.get(3));
		assertEquals("s2", _sList.get(4));
		assertEquals("s5", _sList.get(5));

		sql = "select s.id as sId, s.name as sName, s.age, t.id as tId, t.name as tName from student s left join teacher t on t.id=s.tId";
		rs = st.executeQuery(sql);
		_sList.clear();
		while (rs.next()) {
			_sList.add(rs.getString("sName"));
		}
		assertEquals(6, _sList.size());
		assertEquals("s3", _sList.get(0));
		assertEquals("s6", _sList.get(1));
		assertEquals("s1", _sList.get(2));
		assertEquals("s4", _sList.get(3));
		assertEquals("s2", _sList.get(4));
		assertEquals("s5", _sList.get(5));

		close(null, st, rs);
		System.out.println("relatedQueryTest2 执行结束");
	}

	/**
	 * 准备测试数据
	 * 
	 * @param st
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void prepareRecord(Statement st) throws SQLException {
		st.addBatch("delete from student");
		st.addBatch("delete from teacher");
		st.addBatch("insert into teacher(id,name) values(1,'zhang')");
		st.addBatch("insert into teacher(id,name) values(2,'qian')");
		st.addBatch("insert into teacher(id,name) values(3,'sun')");
		st.addBatch("insert into teacher(id,name) values(4,'wang')");
		st.addBatch("insert into student(id,tId,name,age) values(1,1,'s1',11)");
		st.addBatch("insert into student(id,tId,name,age) values(2,1,'s2',12)");
		st.addBatch("insert into student(id,tId,name,age) values(3,2,'s3',13)");
		st.addBatch("insert into student(id,tId,name,age) values(4,2,'s4',14)");
		st.addBatch("insert into student(id,tId,name,age) values(5,3,'s5',15)");
		st.addBatch("insert into student(id,tId,name,age) values(6,3,'s6',16)");
		st.executeBatch();
	}

	/**
	 * 关闭数据库资源
	 * 
	 * @param conn
	 * @param st
	 * @param rs
	 */
	private static void close(Connection conn, Statement st, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * 清理derby数据库
	 */
	private static void destroy() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true"); // 关闭derby数据库
		} catch (SQLException e) {
			// 关闭失败,忽略之
		}

		FileUtil.deleteFile("derby.log"); // 删除derby日志文件
		FileUtil.deleteFile(DERBY_DBPATH); // 删除derby数据库文件夹

		System.out.println("derby数据库清理完成！");
	}

	/**
	 * 初始化derby数据库和表
	 * 
	 * @throws SQLException
	 */
	private static void init() throws SQLException {
		if (inited) { // 已初始化
			return;
		}

		try {
			Class.forName(TINY_DRIVER); // 加载tiny数据库驱动
			Class.forName(DERBY_DRIVER); // 加载derby数据库驱动
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("数据库驱动加载失败!", e);
		}

		// 初始化routerManager
		routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters(ROUTER_CONFIG);

		FileUtil.deleteFile(DERBY_DBPATH); // 删除derby数据库路径

		String derbyDbpath = DERBY_DBPATH;
		if (!derbyDbpath.endsWith("/") && !derbyDbpath.endsWith("\\")) {
			derbyDbpath = derbyDbpath + "/";
		}
		StringBuffer url = new StringBuffer("jdbc:derby:");
		url.append(derbyDbpath + DERBY_DB);
		url.append(";create=true");

		Connection conn = DriverManager.getConnection(url.toString());
		Statement st = conn.createStatement();
		try {
			for (String teacher : DERBY_TEACHER_TABLES) {
				StringBuffer sb = new StringBuffer();
				sb.append("CREATE TABLE ");
				sb.append(teacher).append("(");
				sb.append("ID int not null,");
				sb.append("NAME varchar(20))");
				st.execute(sb.toString());
			}

			for (String student : DERBY_STUDENT_TABLES) {
				StringBuffer sb = new StringBuffer();
				sb.append("CREATE TABLE ");
				sb.append(student);
				sb.append("(");
				sb.append("ID int not null,");
				sb.append("TID int,");
				sb.append("AGE int,");
				sb.append("NAME varchar(20))");
				st.execute(sb.toString());
			}
		} catch (SQLException e) {
			destroy(); // 关闭derby数据库，并清理对应文件夹
			throw new RuntimeException("初始化表失败!", e);
		} finally {
			close(conn, st, null);
		}

		inited = true;
		System.out.println("数据库和表初始化完成！");
	}

}
