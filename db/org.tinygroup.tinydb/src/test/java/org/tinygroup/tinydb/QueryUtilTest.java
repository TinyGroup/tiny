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
package org.tinygroup.tinydb;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.query.Conditions;
import org.tinygroup.tinydb.test.BaseTest;
import org.tinygroup.tinydb.test.operator.BeanStringOperator;

public class QueryUtilTest extends BaseTest {
	BeanStringOperator queryUtil;

	public void setUp() {
		super.setUp();
		queryUtil = SpringUtil.getBean("beanStringOperator");
	}

	public void testGenerateSqlClause1() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "like", "%aaa%");
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc like ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals("%aaa%", valueList.get(0));

	}

	public void testGenerateSqlClause2() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "like", "%aaa");
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc like ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals("%aaa", valueList.get(0));

	}

	public void testGenerateSqlClause3() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "like", "aaa%");
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc like ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals("aaa%", valueList.get(0));
	}

	public void testGenerateSqlClause4() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "<", 3);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc < ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals(3, valueList.get(0));
	}

	public void testGenerateSqlClause5() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "<=", 3);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc <= ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals(3, valueList.get(0));
	}

	public void testGenerateSqlClause6() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", ">", 3);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc > ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals(3, valueList.get(0));
	}

	public void testGenerateSqlClause7() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", ">=", 3);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc >= ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals(3, valueList.get(0));
	}

	public void testGenerateSqlClause8() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "is null", "");
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc is null", stringBuffer.toString());
		assertEquals(0, valueList.size());
	}

	public void testGenerateSqlClause9() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "is not null", "");
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc is not null", stringBuffer.toString());
		assertEquals(0, valueList.size());
	}

	public void testGenerateSqlClause10() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "=", 3);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc = ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals(3, valueList.get(0));
	}

	public void testGenerateSqlClause11() {
		Conditions conditions = new Conditions();
		conditions.condition("abc", "<>", 3);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc <> ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
		assertEquals(3, valueList.get(0));
	}

	public void testGenerateSqlClause12() {
		Conditions conditions = new Conditions();
		conditions.condition("", "", "");
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("", stringBuffer.toString());
		assertEquals(0, valueList.size());
	}

	public void testGenerateSqlClause13() {
		Conditions conditions = new Conditions();
		conditions.condition("aaa0", "=", 0).and()
				.condition("aaa1", "=", 1);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("aaa0 = ? and aaa1 = ?", stringBuffer.toString());
		assertEquals(2, valueList.size());
		assertEquals(0, valueList.get(0));
		assertEquals(1, valueList.get(1));
	}

	public void testGenerateSqlClause14() {
		Conditions conditions = new Conditions().condition("aaa", "=", 3)
				.and().left().condition("aaa0", "=", 0).and()
				.condition("aaa1", "=", 1).right();
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("aaa = ? and ( aaa0 = ? and aaa1 = ? )",
				stringBuffer.toString());
		assertEquals(3, valueList.size());
		assertEquals(3, valueList.get(0));
		assertEquals(0, valueList.get(1));
		assertEquals(1, valueList.get(2));
	}

	public void testGenerateSqlClause15() {
		Conditions conditions = new Conditions().condition("aaa", "=", 3)
				.or().left().condition("aaa0", "=", 0).or()
				.condition("aaa1", "=", 1).right();
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("aaa = ? or ( aaa0 = ? or aaa1 = ? )",
				stringBuffer.toString());
		assertEquals(3, valueList.size());
		assertEquals(3, valueList.get(0));
		assertEquals(0, valueList.get(1));
		assertEquals(1, valueList.get(2));
	}

	public void testGenerateSqlClause16() {
		Conditions conditions = new Conditions().condition("aaa", "=", 3)
				.or().left().condition("aaa0", "=", 0).and()
				.condition("aaa1", "=", 1).right();
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("aaa = ? or ( aaa0 = ? and aaa1 = ? )",
				stringBuffer.toString());
		assertEquals(3, valueList.size());
		assertEquals(3, valueList.get(0));
		assertEquals(0, valueList.get(1));
		assertEquals(1, valueList.get(2));
	}

	public void testGenerateSqlClause17() {
		Conditions conditions = new Conditions().condition("aaa", "=", 3)
				.and().left().condition("aaa0", "=", 0).or()
				.condition("aaa1", "=", 1).right();
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("aaa = ? and ( aaa0 = ? or aaa1 = ? )",
				stringBuffer.toString());
		assertEquals(3, valueList.size());
		assertEquals(3, valueList.get(0));
		assertEquals(0, valueList.get(1));
		assertEquals(1, valueList.get(2));
	}

	public void testGenerateSqlClause18() {
		int[] intArray = { 3, 4, 5 };
		Conditions conditions = new Conditions();
		conditions.condition("abc", "in", intArray);
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> valueList = new ArrayList<Object>();
		queryUtil.generateQuerySqlClause(conditions, stringBuffer, valueList);
		assertEquals("abc in ?", stringBuffer.toString());
		assertEquals(1, valueList.size());
	}
}
