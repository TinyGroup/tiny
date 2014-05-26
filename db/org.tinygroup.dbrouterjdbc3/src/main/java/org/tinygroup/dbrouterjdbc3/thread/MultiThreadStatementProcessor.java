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
package org.tinygroup.dbrouterjdbc3.thread;

import org.tinygroup.dbrouterjdbc3.jdbc.RealStatementExecutor;
import org.tinygroup.threadgroup.AbstractProcessor;

public class MultiThreadStatementProcessor<T> extends AbstractProcessor {
	
	private StatementProcessorCallBack<T> callBack;

	private RealStatementExecutor statement;
	
	private T resultSet;
	
	public MultiThreadStatementProcessor(String name) {
		super(name);
	}
	

	public MultiThreadStatementProcessor(String name, RealStatementExecutor statement) {
		super(name);
		this.statement = statement;
	}


	protected void action() throws Exception {
		if(callBack!=null){
		    resultSet=callBack.callBack(statement);	
		}
	}
	
	public T getResult(){
		return resultSet;
	}


	public void setCallBack(StatementProcessorCallBack<T> callBack) {
		this.callBack = callBack;
	}
	
	

}
