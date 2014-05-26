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
package org.tinygroup.jsqlparser.statement.select;

import java.util.List;

/**
 * A DISTINCT [ON (expression, ...)] clause
 */
public class Distinct {

	private List<SelectItem> onSelectItems;

	/**
	 * A list of {@link SelectItem}s expressions, as in "select DISTINCT ON
	 * (a,b,c) a,b FROM..."
	 *
	 * @return a list of {@link SelectItem}s expressions
	 */
	public List<SelectItem> getOnSelectItems() {
		return onSelectItems;
	}

	public void setOnSelectItems(List<SelectItem> list) {
		onSelectItems = list;
	}


	public String toString() {
		String sql = "DISTINCT";

		if (onSelectItems != null && onSelectItems.size() > 0) {
			sql += " ON (" + PlainSelect.getStringList(onSelectItems) + ")";
		}

		return sql;
	}
}
