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
package org.tinygroup.jsqlparser.util;

import org.tinygroup.jsqlparser.expression.*;
import org.tinygroup.jsqlparser.statement.select.*;

import java.util.*;

/**
 * Add aliases to every column and expression selected by a select - statement.
 * Existing aliases are recognized and preserved. This class standard uses a
 * prefix of A and a counter to generate new aliases (e.g. A1, A5, ...). This
 * behaviour can be altered.
 *
 * @author tw
 */
public class AddAliasesVisitor implements SelectVisitor, SelectItemVisitor {

	private List<String> aliases = new LinkedList<String>();
	private boolean firstRun = true;
	private int counter = 0;
	private String prefix = "A";


	public void visit(PlainSelect plainSelect) {
		firstRun = true;
		counter = 0;
		aliases.clear();
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(this);
		}
		firstRun = false;
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(this);
		}
	}


	public void visit(SetOperationList setOpList) {
		for (PlainSelect select : setOpList.getPlainSelects()) {
			select.accept(this);
		}
	}


	public void visit(AllTableColumns allTableColumns) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	public void visit(SelectExpressionItem selectExpressionItem) {
		if (firstRun) {
			if (selectExpressionItem.getAlias() != null) {
				aliases.add(selectExpressionItem.getAlias().getName().toUpperCase());
			}
		} else {
			if (selectExpressionItem.getAlias() == null) {

				while (true) {
					String alias = getNextAlias().toUpperCase();
					if (!aliases.contains(alias)) {
						aliases.add(alias);
						selectExpressionItem.setAlias(new Alias(alias));
						break;
					}
				}
			}
		}
	}

	/**
	 * Calculate next alias name to use.
	 *
	 * @return
	 */
	protected String getNextAlias() {
		counter++;
		return prefix + counter;
	}

	/**
	 * Set alias prefix.
	 *
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public void visit(WithItem withItem) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	public void visit(AllColumns allColumns) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
