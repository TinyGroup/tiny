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

import org.tinygroup.jsqlparser.expression.Alias;

/**
 * A table created by "(tab1 join tab2)".
 */
public class SubJoin implements FromItem {

	private FromItem left;
	private Join join;
	private Alias alias;
	private Pivot pivot;


	public void accept(FromItemVisitor fromItemVisitor) {
		fromItemVisitor.visit(this);
	}

	public FromItem getLeft() {
		return left;
	}

	public void setLeft(FromItem l) {
		left = l;
	}

	public Join getJoin() {
		return join;
	}

	public void setJoin(Join j) {
		join = j;
	}


	public Pivot getPivot() {
		return pivot;
	}


	public void setPivot(Pivot pivot) {
		this.pivot = pivot;
	}


	public Alias getAlias() {
		return alias;
	}


	public void setAlias(Alias alias) {
		this.alias = alias;
	}


	public String toString() {
		return "(" + left + " " + join + ")"
				+ ((pivot != null) ? " " + pivot : "")
				+ ((alias != null) ? alias.toString() : "");
	}
}
