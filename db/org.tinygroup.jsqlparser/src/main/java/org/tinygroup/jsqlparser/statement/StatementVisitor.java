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
package org.tinygroup.jsqlparser.statement;

import org.tinygroup.jsqlparser.statement.alter.Alter;
import org.tinygroup.jsqlparser.statement.create.index.CreateIndex;
import org.tinygroup.jsqlparser.statement.create.table.CreateTable;
import org.tinygroup.jsqlparser.statement.create.view.CreateView;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.drop.Drop;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.replace.Replace;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.truncate.Truncate;
import org.tinygroup.jsqlparser.statement.update.Update;

public interface StatementVisitor {

	void visit(Select select);

	void visit(Delete delete);

	void visit(Update update);

	void visit(Insert insert);

	void visit(Replace replace);

	void visit(Drop drop);

	void visit(Truncate truncate);

	void visit(CreateIndex createIndex);

	void visit(CreateTable createTable);

	void visit(CreateView createView);
	
	void visit(Alter alter);
    
    void visit(Statements stmts);
}
