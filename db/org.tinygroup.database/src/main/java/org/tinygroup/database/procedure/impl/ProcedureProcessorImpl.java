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
package org.tinygroup.database.procedure.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.procedure.Procedure;
import org.tinygroup.database.config.procedure.Procedures;
import org.tinygroup.database.procedure.ProcedureProcessor;
import org.tinygroup.database.procedure.ProcedureSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.springutil.SpringUtil;

public class ProcedureProcessorImpl implements ProcedureProcessor {
	Map<String,Procedure> procedureMap = new HashMap<String,Procedure>();
	
	
	public Procedure getProcedure(String procedureName) {
		return procedureMap.get(procedureName);
	}

	public String getCreateSql(String procedureName, String language) {
		Procedure procedure = getProcedure(procedureName);
		if(procedure==null){
			throw new RuntimeException(String.format("过程[name:%s]不存在,",procedureName));
		}
		return getCreateSql(procedure,language);
	}

	public List<String> getCreateSql(String language) {
		List<String> list = new ArrayList<String>();
		for(Procedure procedure:procedureMap.values()){
			list.add(getCreateSql(procedure, language));
		}
		return list;
	}

	private String getCreateSql(Procedure procedure,String language){
		ProcessorManager processorManager = SpringUtil.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		ProcedureSqlProcessor sqlProcessor = (ProcedureSqlProcessor)processorManager.getProcessor(language, "procedure");
		return sqlProcessor.getCreateSql(procedure);
	}
	
	public void addProcedures(Procedures procedures) {
		for(Procedure procedure:procedures.getProcedureList()){
			procedureMap.put(procedure.getName(), procedure);
		}
	}
	
	public void removeProcedures(Procedures procedures) {
		for(Procedure procedure:procedures.getProcedureList()){
			procedureMap.remove(procedure.getName());
		}
	}

	public String getDropSql(String procedureName, String language) {
		Procedure procedure = getProcedure(procedureName);
		if(procedure==null){
			throw new RuntimeException(String.format("过程[name:%s]不存在,",procedureName));
		}
		return getDropSql(procedure,language);
	}

	public List<String> getDropSql(String language) {
		List<String> list = new ArrayList<String>();
		for(Procedure procedure:procedureMap.values()){
			list.add(getDropSql(procedure, language));
		}
		return list;
	}
	private String getDropSql(Procedure procedure,String language){
		ProcessorManager processorManager = SpringUtil.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		ProcedureSqlProcessor sqlProcessor = (ProcedureSqlProcessor)processorManager.getProcessor(language, "procedure");
		return sqlProcessor.getDropSql(procedure);
	}
}
