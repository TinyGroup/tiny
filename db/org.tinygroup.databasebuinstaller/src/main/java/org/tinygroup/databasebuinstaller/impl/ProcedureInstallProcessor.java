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
package org.tinygroup.databasebuinstaller.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.database.procedure.ProcedureProcessor;

/**
 * 
 * 功能说明:存储过程安装处理器 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class ProcedureInstallProcessor extends AbstractInstallProcessor {

	private ProcedureProcessor procedureProcessor;
	
	public ProcedureProcessor getProcedureProcessor() {
		return procedureProcessor;
	}


	public void setProcedureProcessor(ProcedureProcessor procedureProcessor) {
		this.procedureProcessor = procedureProcessor;
	}


	
	protected List<String> getDealSqls(Connection con) throws SQLException {
		List<String> sqls=new ArrayList<String>();
		sqls.addAll(procedureProcessor.getCreateSql(language));
		return sqls;
	}

}
