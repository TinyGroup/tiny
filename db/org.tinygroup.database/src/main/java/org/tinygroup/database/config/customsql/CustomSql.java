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
package org.tinygroup.database.config.customsql;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.database.config.SqlBody;
import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 自定义SQL片段
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("custom-sql")
public class CustomSql extends BaseObject {
	@XStreamAsAttribute
	private String type;// 自定义SQL的类型，可选的有before/after
	@XStreamImplicit
	private List<SqlBody> sqlBodyList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SqlBody> getSqlBodyList() {
		if (sqlBodyList == null)
			sqlBodyList = new ArrayList<SqlBody>();
		return sqlBodyList;
	}

	public void setSqlBodyList(List<SqlBody> sqlBodyList) {
		this.sqlBodyList = sqlBodyList;
	}

}
