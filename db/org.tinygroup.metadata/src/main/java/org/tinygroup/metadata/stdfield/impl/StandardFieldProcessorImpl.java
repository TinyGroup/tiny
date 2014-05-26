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
package org.tinygroup.metadata.stdfield.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.metadata.bizdatatype.BusinessTypeProcessor;
import org.tinygroup.metadata.config.stdfield.NickName;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.config.stdfield.StandardFields;
import org.tinygroup.metadata.stdfield.StandardFieldProcessor;

public class StandardFieldProcessorImpl implements StandardFieldProcessor {
	// private static Map<String, Map<String, StandardField>> standardFieldMap =
	// new HashMap<String, Map<String, StandardField>>();
	private static Map<String, StandardField> standardFieldMap = new HashMap<String, StandardField>();
	private static Map<String,String> nickIdMap = new HashMap<String, String>();
	BusinessTypeProcessor businessTypeProcessor;

	public BusinessTypeProcessor getBusinessTypeProcessor() {
		return businessTypeProcessor;
	}

	public void setBusinessTypeProcessor(
			BusinessTypeProcessor businessTypeProcessor) {
		this.businessTypeProcessor = businessTypeProcessor;
	}

	public StandardField getStandardField(String id) {
		if (standardFieldMap.containsKey(id)) {
			return standardFieldMap.get(id);
		}
		if(nickIdMap.containsKey(id)){
			String realId = nickIdMap.get(id);
			if (standardFieldMap.containsKey(realId)) {
				return standardFieldMap.get(realId);
			}
		}
		throw new RuntimeException(String.format("找不到ID：[%s]的标准字段。", id));
	}

	public void addStandardFields(StandardFields standardFields) {
		if (standardFields != null
				&& standardFields.getStandardFieldList() != null) {
			for (StandardField field : standardFields.getStandardFieldList()) {
				standardFieldMap.put(field.getId(), field);
				if(field.getNickNames()!=null){
					for(NickName name:field.getNickNames()){
						nickIdMap.put(name.getId(), field.getId());
					}
				}
			}
		}
	}
	
	public void removeStandardFields(StandardFields standardFields) {
		if (standardFields != null
				&& standardFields.getStandardFieldList() != null) {
			for (StandardField field : standardFields.getStandardFieldList()) {
				standardFieldMap.remove(field.getId());
				if(field.getNickNames()!=null){
					for(NickName name:field.getNickNames()){
						nickIdMap.remove(name.getId());
					}
				}
			}
		}
	}

	public String getType(String id, String language) {
		StandardField standardField = getStandardField(id);
		String type = businessTypeProcessor.getType(standardField.getTypeId(),
				language);
		if (type != null) {
			return type;
		}
		throw new RuntimeException(String.format(
				"找不到ID：[%s]的标准字段对应的Language:[%s]类型。", id, language));
	}

	

	// public void addStandardFields(StandardFields standardFields) {
	// Map<String, StandardField> nameMap = new HashMap<String,
	// StandardField>();
	// String pkgName = MetadataUtil.passNull(standardFields.getPackageName());
	// standardFieldMap.put(pkgName, nameMap);
	// if (standardFields.getStandardFieldList() != null) {
	// for (StandardField standardField : standardFields
	// .getStandardFieldList()) {
	// nameMap.put(standardField.getName(), standardField);
	// if (standardField.getNickNames() != null) {
	// for (NickName nickName : standardField.getNickNames()) {
	// nameMap.put(nickName.getName(), standardField);
	// }
	// }
	// }
	// }
	// }
	//
	// public String getType(String packageName, String name, String language) {
	// if (packageName != null
	// && standardFieldMap.containsKey(MetadataUtil
	// .passNull(packageName))) {
	// StandardField standardField = standardFieldMap.get(
	// MetadataUtil.passNull(packageName)).get(name);
	// String type = businessTypeProcessor.getType(packageName,
	// standardField.getTypeName(), language);
	// if (type != null) {
	// return type;
	// }
	// }
	// for (String pkgName : standardFieldMap.keySet()) {
	// StandardField standardField = standardFieldMap.get(
	// MetadataUtil.passNull(pkgName)).get(name);
	// String type = businessTypeProcessor.getType(packageName,
	// standardField.getTypeName(), language);
	// if (type != null) {
	// return type;
	// }
	// }
	// throw new RuntimeException(String.format("找不到包名：[%s],名字：[%s]的标准字段。",
	// packageName, name));
	// }
	//
	// public String getType(String name, String language) {
	// return getType(null, name, language);
	// }
	// public StandardField getStandardField(String packageName, String name) {
	// if (packageName != null
	// && standardFieldMap.containsKey(MetadataUtil
	// .passNull(packageName))) {
	// StandardField standardField = standardFieldMap.get(
	// MetadataUtil.passNull(packageName)).get(name);
	// if (standardField != null) {
	// return standardField;
	// }
	//
	// }
	// for (String pkgName : standardFieldMap.keySet()) {
	// StandardField standardField = standardFieldMap.get(
	// MetadataUtil.passNull(pkgName)).get(name);
	// if (standardField != null) {
	// return standardField;
	// }
	// }
	// throw new RuntimeException(String.format("找不到包名：[%s],名字：[%s]的标准字段。",
	// packageName, name));
	// }
}
