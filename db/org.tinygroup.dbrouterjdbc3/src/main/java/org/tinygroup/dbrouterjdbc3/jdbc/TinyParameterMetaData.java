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
package org.tinygroup.dbrouterjdbc3.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;

/**
 * 
 * 功能说明:the parameters of a prepared statement 

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-14 <br>
 * <br>
 */
public class TinyParameterMetaData implements ParameterMetaData {
	
	private ParameterMetaData realMetaData;
	
	
	public TinyParameterMetaData(ParameterMetaData realMetaData) throws SQLException {
		super();
		this.realMetaData = realMetaData;
	}

	public int getParameterCount() throws SQLException {
		return realMetaData.getParameterCount();
	}

	public int isNullable(int param) throws SQLException {
		return realMetaData.isNullable(param);
	}

	public boolean isSigned(int param) throws SQLException {
		return realMetaData.isSigned(param);
	}

	public int getPrecision(int param) throws SQLException {
		return realMetaData.getPrecision(param);
	}

	public int getScale(int param) throws SQLException {
		return realMetaData.getScale(param);
	}

	public int getParameterType(int param) throws SQLException {
		return realMetaData.getParameterType(param);
	}

	public String getParameterTypeName(int param) throws SQLException {
		return realMetaData.getParameterTypeName(param);
	}

	public String getParameterClassName(int param) throws SQLException {
		return realMetaData.getParameterClassName(param);
	}

	public int getParameterMode(int param) throws SQLException {
		return realMetaData.getParameterMode(param);
	}

}
