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
package org.tinygroup.jsqlparser.util.deparser;

import java.util.Iterator;

import org.tinygroup.jsqlparser.statement.create.table.ColumnDefinition;
import org.tinygroup.jsqlparser.statement.create.table.CreateTable;
import org.tinygroup.jsqlparser.statement.create.table.Index;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) a {@link org.tinygroup.jsqlparser.statement.create.table.CreateTable}
 */
public class CreateTableDeParser {

	private StringBuilder buffer;

	/**
	 * @param buffer the buffer that will be filled with the select
	 */
	public CreateTableDeParser(StringBuilder buffer) {
		this.buffer = buffer;
	}

	public void deParse(CreateTable createTable) {
		buffer.append("CREATE TABLE ").append(createTable.getTable().getFullyQualifiedName());
		if (createTable.getColumnDefinitions() != null) {
			buffer.append(" (");
			for (Iterator<ColumnDefinition> iter = createTable.getColumnDefinitions().iterator(); iter.hasNext();) {
				ColumnDefinition columnDefinition = iter.next();
				buffer.append(columnDefinition.getColumnName());
				buffer.append(" ");
				buffer.append(columnDefinition.getColDataType().toString());
				if (columnDefinition.getColumnSpecStrings() != null) {
                    for (String s : columnDefinition.getColumnSpecStrings()) {
                        buffer.append(" ");
                        buffer.append(s);
                    }
				}

				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}

			if (createTable.getIndexes() != null) {
				for (Iterator<Index> iter = createTable.getIndexes().iterator(); iter.hasNext();) {
					buffer.append(", ");
					Index index = iter.next();
					buffer.append(index.toString());
				}
			}

			buffer.append(")");
		}
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}
}
