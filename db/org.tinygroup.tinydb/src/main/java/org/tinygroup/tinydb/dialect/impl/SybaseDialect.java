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

import org.springframework.jdbc.support.incrementer.SybaseMaxValueIncrementer;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.database.dialectfunction.DialectFunctionProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.dialect.Dialect;

public class SybaseDialect implements Dialect {
	
	private SybaseMaxValueIncrementer incrementer;
	

	public SybaseMaxValueIncrementer getIncrementer() {
		return incrementer;
	}

	public void setIncrementer(SybaseMaxValueIncrementer incrementer) {
		this.incrementer = incrementer;
	}

	public SybaseDialect() {

	}

	public int getNextKey() {
		Assert.assertNotNull(incrementer,"incrementer must not null");
		return incrementer.nextIntValue();
	}

	
	public String getCurrentDate() {
		return "Select CONVERT(varchar(100), GETDATE(), 21)";
	}

	public String getLimitString(String sql, int offset, int limit) {
		throw new UnsupportedOperationException( "paged queries not supported" );
	}

	public boolean supportsLimit() {
		return false;
	}
	
	public String buildSqlFuction(String sql) {
		DialectFunctionProcessor processor=SpringUtil.getBean(DataBaseUtil.FUNCTION_BEAN);
		return processor.getFuntionSql(sql, DataBaseUtil.DB_TYPE_SYBASE);
	}
}
