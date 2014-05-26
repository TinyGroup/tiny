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

import org.tinygroup.jsqlparser.statement.create.index.CreateIndex;
import org.tinygroup.jsqlparser.statement.create.table.Index;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string)
 * a {@link org.tinygroup.jsqlparser.statement.create.index.CreateIndex}
 *
 * @author Raymond Augé
 */
public class CreateIndexDeParser {

    private StringBuilder buffer;

	/**
	 * @param buffer the buffer that will be filled with the create
	 */
	public CreateIndexDeParser(StringBuilder buffer) {
		this.buffer = buffer;
	}

	public void deParse(CreateIndex createIndex) {
		Index index = createIndex.getIndex();

		buffer.append("CREATE ");

		if (index.getType() != null) {
			buffer.append(index.getType());
			buffer.append(" ");
		}

		buffer.append("INDEX ");
		buffer.append(index.getName());
		buffer.append(" ON ");
		buffer.append(createIndex.getTable().getFullyQualifiedName());

		if (index.getColumnsNames() != null) {
			buffer.append(" (");
			for (Iterator iter = index.getColumnsNames().iterator(); iter.hasNext();) {
				String columnName = (String)iter.next();
				buffer.append(columnName);

				if (iter.hasNext()) {
					buffer.append(", ");
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
