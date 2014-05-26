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

import org.tinygroup.database.config.SqlBody;
import org.tinygroup.database.config.procedure.ParameterType;
import org.tinygroup.database.config.procedure.Procedure;
import org.tinygroup.database.config.procedure.ProcedureParameter;
import org.tinygroup.database.procedure.ProcedureSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;

public abstract class ProcedureSqlProcessorImpl implements
		ProcedureSqlProcessor {
	protected abstract String getDatabaseType();

	public String getCreateSql(Procedure procedure) {
		StringBuffer sb = new StringBuffer();
		appendHead(sb, procedure);
		appendParams(sb, procedure);
		appendBody(sb, procedure);
		return sb.toString();
	}

	private void appendHead(StringBuffer sb, Procedure procedure) {
		sb.append("CREATE OR REPLACE PROCEDURE ");
		sb.append(procedure.getName());
	}

	private void appendParams(StringBuffer sb, Procedure procedure) {
		if (procedure.getParameterList().size() == 0) {
			return;
		}
		sb.append("(").append("\n");
		for (ProcedureParameter param : procedure.getParameterList()) {
			if (param.getParameterType() == ParameterType.IN) {
				appendParam(sb, "IN", param, procedure);
			} else if (param.getParameterType() == ParameterType.OUT) {
				appendParam(sb, "OUT", param, procedure);
			} else if (param.getParameterType() == ParameterType.IN) {
				appendParam(sb, "IN OUT", param, procedure);
			}
		}
		// 去掉多余的,
		sb.delete(sb.length() - 1, sb.length());
		sb.append(")").append("\n");
	}

	private void appendParam(StringBuffer sb, String type,
			ProcedureParameter param, Procedure procedure) {
		String id = param.getStandardFieldId();
		String defaultValue = param.getDefaultValue();
		StandardField field = MetadataUtil.getStandardField(id);
		String name = DataBaseUtil.getDataBaseName(field.getName());
		String dataType = MetadataUtil.getStandardFieldType(id,
				getDatabaseType());
		String paramStr = "";
		if (defaultValue != null) {
			paramStr = String.format("%s %s %s default %s,", name, type,
					dataType, defaultValue);
		} else {
			paramStr = String.format("%s %s %s,", name, type, dataType);
		}
		sb.append(paramStr);
	}

	private void appendBody(StringBuffer sb, Procedure procedure) {
		for (SqlBody sql : procedure.getProcedureBodyList()) {
			if ("oracle".equals(sql.getDialectTypeName())) {
				sb.append(" IS ").append("\n");
				sb.append(" BEGIN ").append("\n");
				sb.append(sql.getContent());
				sb.append("\n").append(" END ");
				sb.append(procedure.getName()).append(";").append("\n");
			}
		}
	}


	public String getDropSql(Procedure procedure) {
		return "DROP PROCEDURE " + procedure.getName() + ";";
	}

}
