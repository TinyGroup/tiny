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
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;

public class AggregateTest extends TestCase {
	static Connection conn;
	static Statement stmt;
	static {
		try {
			prepareRecord();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void prepareRecord() throws Exception {
		// 删除数据
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/differentSchemaAggregate.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		conn = DriverManager.getConnection("jdbc:dbrouter://aggregate", "luog",
				"123456");
		stmt = conn.createStatement();
		stmt.execute("delete from score");
		stmt.executeUpdate("insert into score(id,name,score,course) values(1,'xiaohuihui',99,'shuxue')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(2,'xiaohuihui',97,'yuwen')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(3,'xiaom',95,'shuxue')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(4,'xiaof',97,'yingyu')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(5,'xiaom',100,'yuwen')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(6,'xiaof',95,'yuwen')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(7,'xiaohuihui',95,'yingyu')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(8,'xiaom',96,'yingyu')");
		stmt.executeUpdate("insert into score(id,name,score,course) values(9,'xiaof',96,'shuxue')");
	}

	public void testCount() throws SQLException {
		String sql = "select count(*),name from score group by name";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			String name = resultSet.getString(2);
			if (name.equals("xiaohuihui")) {
				assertEquals(3, resultSet.getInt(1));
			} else if (name.equals("xiaom")) {
				assertEquals(3, resultSet.getInt(1));
			} else if (name.equals("xiaof")) {
				assertEquals(3, resultSet.getInt(1));
			}
		}
	}

	public void testMax() throws SQLException {
		String sql = "select max(score) score,course from score group by course";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			String course = resultSet.getString(2);
			if (course.equals("shuxue")) {
				assertEquals(99, resultSet.getInt(1));
			} else if (course.equals("yingyu")) {
				assertEquals(97, resultSet.getInt(1));
			} else if (course.equals("yuwen")) {
				assertEquals(100, resultSet.getInt(1));
			}
		}
	}

	public void testMaxSingle() throws SQLException {
		String sql = "select max(score) score from score";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.next();
		assertEquals(100, resultSet.getInt(1));
	}

	public void testSum() throws SQLException {
		String sql = "select sum(score) score,name from score group by name";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			String name = resultSet.getString(2);
			if (name.equals("xiaohuihui")) {
				assertEquals(291, resultSet.getInt(1));
			} else if (name.equals("xiaom")) {
				assertEquals(291, resultSet.getInt(1));
			} else if (name.equals("xiaof")) {
				assertEquals(288, resultSet.getInt(1));
			}
		}
	}

	public void testMin() throws SQLException {
		String sql = "select min(score) score,name from score group by name";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			String name = resultSet.getString(2);
			if (name.equals("xiaohuihui")) {
				assertEquals(95, resultSet.getInt(1));
			} else if (name.equals("xiaom")) {
				assertEquals(95, resultSet.getInt(1));
			} else if (name.equals("xiaof")) {
				assertEquals(95, resultSet.getInt(1));
			}
		}
	}

	public void testMinSingle() throws SQLException {
		String sql = "select min(score) score from score";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.next();
		assertEquals(95, resultSet.getInt(1));
	}

	public void testAvg() throws SQLException {
		String sql = "select avg(score) score,name from score group by name";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			String name = resultSet.getString(2);
			if (name.equals("xiaohuihui")) {
				assertEquals(97.0, resultSet.getDouble(1));
			} else if (name.equals("xiaom")) {
				assertEquals(97.0, resultSet.getDouble(1));
			} else if (name.equals("xiaof")) {
				assertEquals(96.0, resultSet.getDouble(1));
			}
		}
	}

	public void testMultiWithOrderby() throws SQLException {
		String sql = "select min(score) minscore,max(score) maxscore,sum(score) sumscore,avg(score) avgscore, name from score group by name order by name";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			String name = resultSet.getString("name");
			if (name.equals("xiaohuihui")) {
				assertEquals(95.0, resultSet.getDouble(1));
				assertEquals(99.0, resultSet.getDouble(2));
				assertEquals(291.0, resultSet.getDouble(3));
				assertEquals(97.0, resultSet.getDouble(4));
			} else if (name.equals("xiaom")) {
				assertEquals(95.0, resultSet.getDouble(1));
				assertEquals(100.0, resultSet.getDouble(2));
				assertEquals(291.0, resultSet.getDouble(3));
				assertEquals(97.0, resultSet.getDouble(4));
			} else if (name.equals("xiaof")) {
				assertEquals(95.0, resultSet.getDouble(1));
				assertEquals(97.0, resultSet.getDouble(2));
				assertEquals(288.0, resultSet.getDouble(3));
				assertEquals(96.0, resultSet.getDouble(4));
			}
		}
	}

	public void testMultiSingle() throws SQLException {
		String sql = "select min(score) minscore,max(score) maxscore,sum(score) sumscore,avg(score) avgscore from score";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.next();
		assertEquals(95.0, resultSet.getDouble(1));
		assertEquals(100.0, resultSet.getDouble(2));
		assertEquals(870.0, resultSet.getDouble(3));
		assertEquals(97.0, Math.ceil(resultSet.getDouble(4)));
	}

	public void testMaxWithFirstAndLast() throws SQLException {
		String sql = "select max(score) score,name,course from score group by name order by score";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.absolute(1);
		assertEquals(97, resultSet.getInt(1));
		assertEquals("xiaof", resultSet.getString(2));
		resultSet.first();
		assertTrue(resultSet.isFirst());
		assertEquals(97, resultSet.getInt(1));
		assertEquals("xiaof", resultSet.getString(2));
		resultSet.last();
		assertTrue(resultSet.isLast());
		assertEquals(100, resultSet.getInt(1));
		assertEquals("xiaom", resultSet.getString(2));

	}

	public void testMaxWithOrderBy() throws SQLException {
		String sql = "select max(score) score,course from score group by course order by score";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.next();
		assertEquals("yingyu", resultSet.getString(2));
		assertEquals(97, resultSet.getInt(1));
		resultSet.next();
		assertEquals("shuxue", resultSet.getString(2));
		assertEquals(99, resultSet.getInt(1));
		resultSet.next();
		assertEquals("yuwen", resultSet.getString(2));
		assertEquals(100, resultSet.getInt(1));
	}

}
