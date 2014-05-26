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
package org.tinygroup.metadata.stddatatype.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.metadata.config.stddatatype.DialectType;
import org.tinygroup.metadata.config.stddatatype.StandardType;
import org.tinygroup.metadata.config.stddatatype.StandardTypes;
import org.tinygroup.metadata.stddatatype.StandardTypeProcessor;
import org.tinygroup.metadata.util.MetadataUtil;

/**
 * 
 * @author luoguo
 * 
 */
public class StandardTypeProcessorImpl implements StandardTypeProcessor {
	// package/type/standardtype
	// private static Map<String, Map<String, StandardType>> standardMap = new
	// HashMap<String, Map<String, StandardType>>();
	private static Map<String, StandardType> standardMap = new HashMap<String, StandardType>();

	public String getType(String id, String language) {
		// return getType(null, language, id);
		StandardType standardType = standardMap.get(id);
		if (standardType == null) {
			throw new RuntimeException(String.format(
					"不存在, 标准类型ID:[%s],对应的标准数据类型", id, language));
		}
		if (standardType.getDialectTypeList() != null) {
			for (DialectType dialectType : standardType.getDialectTypeList()) {
				if (dialectType.getLanguage().equals(language)) {
					return MetadataUtil.formatType(dialectType.getType(),
							dialectType.getPlaceholderValueList());
				}
			}
		}
		throw new RuntimeException(String.format(
				"不存在, 标准类型ID:[%s],语言:[%s]对应的类型 ", id, language));
	}

	public void addStandardTypes(StandardTypes standardTypes) {
		if (standardTypes != null
				&& standardTypes.getStandardTypeList() != null) {
			for (StandardType standardType : standardTypes
					.getStandardTypeList()) {
				standardMap.put(standardType.getId(), standardType);
			}
		}
	}
	
	public void removeStandardTypes(StandardTypes standardTypes) {
		if (standardTypes != null
				&& standardTypes.getStandardTypeList() != null) {
			for (StandardType standardType : standardTypes
					.getStandardTypeList()) {
				standardMap.remove(standardType.getId());
			}
		}
	}

	public StandardType getStandardType(String id) {
		if(standardMap.containsKey(id))
			return standardMap.get(id);
		throw new RuntimeException(String.format("不存在ID:[%s]对应的标准数据类型 ", id));
	}

	// private String getPackageName(String packageName) {
	// if (packageName == null) {
	// return "";
	// }
	// return packageName;
	// }
	// public void addStandardTypes(StandardTypes standardTypes) {
	// Map<String, StandardType> stdMap = new HashMap<String, StandardType>();
	// standardMap.put(getPackageName(standardTypes.getPackageName()), stdMap);
	// if (standardTypes.getStandardTypeList() != null) {
	// for (StandardType standardType : standardTypes
	// .getStandardTypeList()) {
	// stdMap.put(standardType.getId(), standardType);
	// }
	// }
	// }
	// public String getType(String packageName, String type, String language) {
	// if (packageName != null) {
	// Map<String, StandardType> nameMap = standardMap
	// .get(getPackageName(packageName));
	// if (nameMap != null) {
	// StandardType standardType = nameMap.get(type);
	// if (standardType.getDialectTypeList() != null) {
	// for (DialectType dialectType : standardType
	// .getDialectTypeList()) {
	// if (dialectType.getLanguage().equals(language)) {
	// return MetadataUtil.formatType(
	// dialectType.getType(),
	// dialectType.getPlaceholderValueList());
	// }
	// }
	// }
	// }
	// }
	// for (String pkgName : standardMap.keySet()) {
	// Map<String, StandardType> nameMap = standardMap
	// .get(getPackageName(pkgName));
	// if (nameMap != null) {
	// StandardType standardType = nameMap.get(type);
	// if (standardType.getDialectTypeList() != null) {
	// for (DialectType dialectType : standardType
	// .getDialectTypeList()) {
	// if (dialectType.getLanguage().equals(language)) {
	// return MetadataUtil.formatType(
	// dialectType.getType(),
	// dialectType.getPlaceholderValueList());
	// }
	// }
	// }
	// }
	// }
	// throw new RuntimeException(String.format(
	// "不存在，包：[%s], 标准类型:[%s],语言:[%s]对应的类型 ", packageName, type,
	// language));
	// }

}
