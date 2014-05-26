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
package org.tinygroup.tinydb.test.nameconverter;

import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.impl.DefaultNameConverter;

import junit.framework.TestCase;

public class NameConverterTest extends TestCase {
	private BeanDbNameConverter converter = new DefaultNameConverter();
	
	public void testPropertyToDB(){
		assertEquals("a_b", converter.propertyNameToDbFieldName("aB"));
		assertEquals("a_b_c", converter.propertyNameToDbFieldName("aBC"));
		assertEquals("a_bb_cc", converter.propertyNameToDbFieldName("aBbCc"));
		assertEquals("a", converter.propertyNameToDbFieldName("a"));
	}
	
	public void testDBToProperty(){
		assertEquals("aB", converter.dbFieldNameToPropertyName("A_B"));
		assertEquals("aBC", converter.dbFieldNameToPropertyName("A_B_C"));
		assertEquals("aBbCc", converter.dbFieldNameToPropertyName("A_Bb_Cc"));
		assertEquals("aBbCc", converter.dbFieldNameToPropertyName("A_BB_CC"));
		assertEquals("abbcc", converter.dbFieldNameToPropertyName("ABBCC"));
		assertEquals("abbcc", converter.dbFieldNameToPropertyName("aBBcC"));
	}
	
	public void testDBToPojo(){
		assertEquals("AB", converter.dbTableNameToTypeName("A_B"));
		assertEquals("ABC", converter.dbTableNameToTypeName("A_B_C"));
		assertEquals("ABbCc", converter.dbTableNameToTypeName("A_Bb_Cc"));
		assertEquals("ABbCc", converter.dbTableNameToTypeName("A_BB_CC"));
		assertEquals("Abbcc", converter.dbTableNameToTypeName("ABBCC"));
		assertEquals("Abbcc", converter.dbTableNameToTypeName("aBBcC"));
	}
	public void testPojoToDB(){
		assertEquals("a_b", converter.typeNameToDbTableName("AB"));
		assertEquals("a_b_c", converter.typeNameToDbTableName("ABC"));
		assertEquals("a_bb_cc", converter.typeNameToDbTableName("ABbCc"));
		assertEquals("a", converter.typeNameToDbTableName("A"));
	}
}
