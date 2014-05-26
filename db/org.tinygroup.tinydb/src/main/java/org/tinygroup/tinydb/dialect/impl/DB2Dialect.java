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

import org.springframework.jdbc.support.incrementer.DB2SequenceMaxValueIncrementer;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.database.dialectfunction.DialectFunctionProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.dialect.Dialect;

public class DB2Dialect implements Dialect{

	private DB2SequenceMaxValueIncrementer incrementer;
	
	public DB2SequenceMaxValueIncrementer getIncrementer() {
		return incrementer;
	}

	public void setIncrementer(DB2SequenceMaxValueIncrementer incrementer) {
		this.incrementer = incrementer;
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset, int limit) {
		 return getLimitString( sql, offset > 0 );
	}
	
	private String getLimitString(String sql, boolean hasOffset) {

		int startOfSelect = sql.toLowerCase().indexOf("select");

		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 )
					.append( sql.substring(0, startOfSelect) ) //add the comment
					.append("select * from ( select ") //nest the main query in an outer select
					.append( getRowNumber(sql) ); //add the rownnumber bit into the outer query select list

		if ( hasDistinct(sql) ) {
			pagingSelect.append(" row_.* from ( ") //add another (inner) nested select
				.append( sql.substring(startOfSelect) ) //add the main query
				.append(" ) as row_"); //close off the inner nested select
		}
		else {
			pagingSelect.append( sql.substring( startOfSelect + 6 ) ); //add the main query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		//add the restriction to the outer select
		if (hasOffset) {
			pagingSelect.append("between ?+1 and ?");
		}
		else {
			pagingSelect.append("<= ?");
		}

		return pagingSelect.toString();
	}
	

	public int getNextKey() {
		Assert.assertNotNull(incrementer,"incrementer must not null");
		return incrementer.nextIntValue();
	}

	/**
	 * Render the <tt>rownumber() over ( .... ) as rownumber_,</tt> 
	 * bit, that goes in the select list
	 */
	private String getRowNumber(String sql) {
		StringBuffer rownumber = new StringBuffer(50)
			.append("rownumber() over(");

		int orderByIndex = sql.toLowerCase().indexOf("order by");

		if ( orderByIndex>0 && !hasDistinct(sql) ) {
			rownumber.append( sql.substring(orderByIndex) );
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
	}


	/**
	 * Checks for distinct.
	 * 
	 * @param sql
	 *            the sql
	 * @return true, if successful
	 */
	private  boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}

	public String getCurrentDate() {
		return "values current timestamp";
	}

	public String buildSqlFuction(String sql) {
		DialectFunctionProcessor processor=SpringUtil.getBean(DataBaseUtil.FUNCTION_BEAN);
		return processor.getFuntionSql(sql, DataBaseUtil.DB_TYPE_DB2);
	}

}
