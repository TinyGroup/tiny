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

import org.tinygroup.jsqlparser.statement.select.SetOperationList.SetOperationType;

/**
 *
 * @author tw
 */
public class UnionOp extends SetOperation {

	private boolean distinct;
	private boolean all;

	public UnionOp() {
		super(SetOperationType.UNION);
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}


	public String toString() {
		String allDistinct = "";
		if (isAll()) {
			allDistinct = " ALL";
		} else if (isDistinct()) {
			allDistinct = " DISTINCT";
		}
		return super.toString() + allDistinct;
	}
}
