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

import org.tinygroup.jsqlparser.statement.create.view.CreateView;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) a {@link org.tinygroup.jsqlparser.statement.create.view.CreateView}
 */
public class CreateViewDeParser {

	private StringBuilder buffer;

	/**
	 * @param buffer the buffer that will be filled with the select
	 */
	public CreateViewDeParser(StringBuilder buffer) {
		this.buffer = buffer;
	}

	public void deParse(CreateView createView) {
		buffer.append("CREATE ");
		if (createView.isOrReplace()) {
			buffer.append("OR REPLACE ");
		}
		if (createView.isMaterialized()) {
			buffer.append("MATERIALIZED ");
		}
		buffer.append("VIEW ").append(createView.getView().getFullyQualifiedName());
		if (createView.getColumnNames() != null) {
			buffer.append(PlainSelect.getStringList(createView.getColumnNames(), true, true));
		}
		buffer.append(" AS ");
		buffer.append(createView.getSelectBody().toString());
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}
}
