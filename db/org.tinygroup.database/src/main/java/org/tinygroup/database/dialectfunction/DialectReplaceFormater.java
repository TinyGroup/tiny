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
package org.tinygroup.database.dialectfunction;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.database.config.dialectfunction.Dialect;
import org.tinygroup.format.FormatProvider;
import org.tinygroup.format.exception.FormatException;


/**
 * 
 * 功能说明:替换sql语句中的方言函数 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-20 <br>
 * <br>
 */
public class DialectReplaceFormater  implements FormatProvider{

	private DialectFunctionProcessor processor;
	
	
	public DialectReplaceFormater(DialectFunctionProcessor processor) {
		super();
		this.processor = processor;
	}


	public String format(Context context, String string) throws FormatException {
		String databaseType=context.get(DialectFunctionProcessor.DATABASE_TYPE);
		return format(string, databaseType);
	}
	

	
	private String format(String sqlFrag,String databaseType){
		String returnStr = sqlFrag;
		int index=sqlFrag.indexOf("(");
		int endIndex=sqlFrag.lastIndexOf(")");
		if(index!=-1&&endIndex!=-1){
			String functionName=sqlFrag.substring(0, index);
			String expression=sqlFrag.substring(index+1, endIndex);
			Dialect dialect=processor.getDialectWithDatabaseType(functionName, databaseType);
			if(dialect!=null){
				String dialectFunctionName=dialect.getFunctionName();
					String[] params=analyzeFunc(expression);
					int separatorIndex=dialectFunctionName.indexOf("@");
					if(separatorIndex!=-1){
						returnStr=transSqlWithMutiParam(dialectFunctionName, params,
								separatorIndex);
					}else{
						returnStr = transSqlWithParams(
								dialectFunctionName, params);
					}
			}
		}
		
		return returnStr;
	}


	private String transSqlWithParams(String dialectFunctionName,
			String[] params) {
		String returnStr=dialectFunctionName;
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			returnStr=returnStr.replaceAll("\\$"+(i+1), param);
		}
		return returnStr;
	}


	private String transSqlWithMutiParam(String dialectFunctionName,
			String[] params, int separatorIndex) {
		StringBuffer buffer=new StringBuffer();
		buffer.append(dialectFunctionName.substring(0, separatorIndex));
		int separatorEndIndex= dialectFunctionName.indexOf(")");
		String separator=dialectFunctionName.substring(separatorIndex+1, separatorEndIndex);
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			buffer.append(param);
			if(i!=params.length-1){
				buffer.append(separator);
			}
		}
		buffer.append(dialectFunctionName.substring(separatorEndIndex,dialectFunctionName.length()));
		return buffer.toString();
	}
	
	private  String[] analyzeFunc(String srcSql) {
		List<String> paraList=new ArrayList<String>();
		boolean markers = false;
		StringBuilder sb = new StringBuilder();
		char[] sqlCharArray = srcSql.toCharArray();		
		for (int i = 0; i < sqlCharArray.length; i++) {

			if (sqlCharArray[i] == '(') {
				markers = true;
			}
			if (markers && ')' == sqlCharArray[i]) {
				markers = false;
			}
			if (sqlCharArray[i] == ','&&!markers ) {
                	paraList.add(sb.toString().trim());
					sb = new StringBuilder();
			}else{
				sb.append(sqlCharArray[i]);
			}
			if(i==sqlCharArray.length-1){//最后加入之前解析的字符串
				paraList.add(sb.toString().trim());
			}
			
		}
		return paraList.toArray(new String[0]);
	}

	
}
