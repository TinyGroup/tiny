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
package org.tinygroup.database.config.procedure;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.database.config.SqlBody;
import org.tinygroup.database.config.UsePackage;
import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 存储过程
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("procedure")
public class Procedure extends BaseObject {
	@XStreamAsAttribute
	private String schema;
	@XStreamAlias("use-packages")
	List<UsePackage> usePackages;
	@XStreamAlias("procedure-parameters")
	private List<ProcedureParameter> parameterList;
	@XStreamAlias("sqls")
	private List<SqlBody> procedureBodyList;
	
	public String getName() {
		if (getSchema() == null || "".equals(getSchema()))
			return super.getName();
		return String.format("%s.%s", getSchema(), super.getName());
	}
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	public List<UsePackage> getUsePackages() {
		if (usePackages == null)
			usePackages = new ArrayList<UsePackage>();
		return usePackages;
	}

	public void setUsePackages(List<UsePackage> usePackages) {
		this.usePackages = usePackages;
	}

	public List<ProcedureParameter> getParameterList() {
		if (parameterList == null)
			parameterList = new ArrayList<ProcedureParameter>();
		return parameterList;
	}

	public void setParameterList(List<ProcedureParameter> parameterList) {
		this.parameterList = parameterList;
	}

	public List<SqlBody> getProcedureBodyList() {
		if (procedureBodyList == null)
			procedureBodyList = new ArrayList<SqlBody>();
		return procedureBodyList;
	}

	public void setProcedureBodyList(List<SqlBody> procedureBodyList) {
		this.procedureBodyList = procedureBodyList;
	}

}
