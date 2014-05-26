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
package org.tinygroup.tinydb.dialect.impl;

import javax.sql.DataSource;

import org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer;


/**
 * informix 主键自增长实现
 * 功能说明: 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-5 <br>
 * <br>
 */
public class InformixSequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {

	
	public InformixSequenceMaxValueIncrementer() {
		super();
	}

	public InformixSequenceMaxValueIncrementer(DataSource dataSource,
			String incrementerName) {
		super(dataSource, incrementerName);
	}

	
	protected String getSequenceQuery() {
		return "select " + getSelectSequenceNextValString( getIncrementerName() ) + " from systables where tabid=1";
	}

	private String getSelectSequenceNextValString(String sequenceName) {
		return sequenceName + ".nextval";
	}

}
