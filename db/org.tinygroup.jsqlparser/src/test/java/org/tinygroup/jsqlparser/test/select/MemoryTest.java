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
package org.tinygroup.jsqlparser.test.select;

import java.io.StringReader;

import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.Statement;

public class MemoryTest {

	public static void main(String[] args) throws Exception {
		System.gc();
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		CCJSqlParserManager parserManager = new CCJSqlParserManager();

		/*
		 * String longQuery = new String(
		 * "select  *  from  (  SELECT  intermediate.id  as  id  ,  intermediate.date  as          " +
		 * "date  FROM  (  SELECT  DISTINCT   (  id  )   FROM  (  SELECT                           " +
		 * "wct_workflows.workflow_id  as  id  ,  wct_transaction.date  as  date  FROM             " +
		 * "wct_audit_entry  ,  wct_transaction  ,  wct_workflows  WHERE                           " +
		 * "(  wct_audit_entry.privilege  =  'W'  or  wct_audit_entry.privilege  =                 " +
		 * "'C'  )  and  wct_audit_entry.outcome  =  't'  and                                      " +
		 * "wct_audit_entry.transaction_id  =  wct_transaction.transaction_id  and                 " +
		 * "wct_transaction.user_id  = 164 and  wct_audit_entry.object_id  =                       " +
		 * "wct_workflows.active_version_id ))) UNION  SELECT  wct_workflows.workflow_id  as       " +
		 * "id  ,  wct_transaction.date  as  date  FROM  wct_audit_entry  ,                        " +
		 * "wct_transaction  ,  wct_workflows  WHERE  (  wct_audit_entry.privilege  =              " +
		 * "'W'  or  wct_audit_entry.privilege  =  'C'  )  and  wct_audit_entry.outcome            " +
		 * "=  't'  and  wct_audit_entry.transaction_id  =                                         " +
		 * "wct_transaction.transaction_id  and  wct_transaction.user_id  = 164 and                " +
		 * "afdf=  (  select  wct_audit_entry.object_id  from  wct_audit_entry  ,                  " +
		 * "wct_workflow_archive  where  wct_audit_entry.object_id  =                              " +
		 * "wct_workflow_archive.archive_id  and  wct_workflows.workflow_id  =                     " +
		 * "wct_workflow_archive.workflow_id  )                                                    " +
		 * "UNION  SELECT  wct_workflows.workflow_id                                               " +
		 * "as  id  ,  wct_transaction.date  as  date  FROM  wct_audit_entry  ,                    " +
		 * "wct_transaction  ,  wct_workflows  WHERE  (  wct_audit_entry.privilege  =              " +
		 * "'W'  OR  wct_audit_entry.privilege  =  'E'  OR  wct_audit_entry.privilege  =           " +
		 * "'A'  )  and  wct_audit_entry.outcome  =  't'  and                                      " +
		 * "wct_audit_entry.transaction_id  =  wct_transaction.transaction_id  and                 " +
		 * "wct_transaction.user_id  = 164 and  wct_audit_entry.object_id  =                       " +
		 * "wct_workflows.workflow_id    UNION SELECT * FROM interm2  ,  wct_workflow_docs  WHERE  " +
		 * "interm2.id  =  wct_workflow_docs.document_id  ORDER BY  id  ,  date  DESC              ");
		 */
		String longQuery = new String("select * from k where ID > 4");

		/*
		 * String longQuery = "select  *  from  (  SELECT  intermediate.id  as  id  ,  intermediate.date  as          "
		 * + "date  FROM  (  SELECT  DISTINCT   (  id  )   FROM  (  SELECT                           " +
		 * "wct_workflows.workflow_id  as  id  ,  wct_transaction.date  as  date  FROM             " +
		 * "wct_audit_entry  ,  wct_transaction  ,  wct_workflows  WHERE                           " +
		 * "(  wct_audit_entry.privilege  =  'W'  or  wct_audit_entry.privilege  =                 " + "'C'  ))))";
		 */
		/*
		 * String longQuery = "select  *  from  d WHERE                           " +
		 * "(  wct_audit_entry.privilege  =  'W'  or  wct_audit_entry.privilege  =                 " +
		 * "'C'  )  and  wct_audit_entry.outcome  =  't'  and                                      " +
		 * "wct_audit_entry.transaction_id  =  wct_transaction.transaction_id  and                 " +
		 * "wct_transaction.user_id  = 164 and  wct_audit_entry.object_id  =                       " +
		 * "wct_workflows.active_version_id ";
		 */
		StringReader stringReader = new StringReader(longQuery);
		Statement statement = parserManager.parse(stringReader);
		// stringReader = new StringReader(longQuery);
		// Statement statement2 = parserManager.parse(stringReader);
		// stringReader = null;
		// statement2 = null;
		statement = null;
		parserManager = null;
		longQuery = null;
		System.gc();
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

	}
}
