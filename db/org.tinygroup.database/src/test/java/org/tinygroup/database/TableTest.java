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
package org.tinygroup.database;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.database.config.table.Index;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.config.table.Tables;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class TableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XStream stream = XStreamFactory.getXStream(DataBaseUtil.DATABASE_XSTREAM);
		stream.processAnnotations(Tables.class);

		Tables tables=new Tables();
		tables.setName("aa");
		tables.setTitle("AA");
		tables.setPackageName("aa.bb");
		List<Table> tableList=new ArrayList<Table>();;
		tables.setTableList(tableList);
		Table table=new Table();;
		tableList.add(table);
		table.setName("aa");
		table.setTitle("AA");
		List<TableField> fieldList=new ArrayList<TableField>();
		table.setFieldList(fieldList);
		TableField field=new TableField();
		field.setPrimary(true);
		field.setStandardFieldId("aa");
		fieldList.add(field);
		
		Index index =new Index();
		index.getFields().add("aa");
		index.getFields().add("aa");
		table.getIndexList().add(index);
		table.getIndexList().add(index);
		System.out.println(stream.toXML(tables));
	}

}
