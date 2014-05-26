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
package org.tinygroup.metadata.bizdatatype.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.metadata.bizdatatype.BusinessTypeProcessor;
import org.tinygroup.metadata.config.bizdatatype.BusinessType;
import org.tinygroup.metadata.config.bizdatatype.BusinessTypes;
import org.tinygroup.metadata.stddatatype.StandardTypeProcessor;
import org.tinygroup.metadata.util.MetadataUtil;

public class BusinessTypeProcessorImpl implements BusinessTypeProcessor {
	// packagename/name/businessType
	//private static Map<String, Map<String, BusinessType>> businessTypeMap = new HashMap<String, Map<String, BusinessType>>();
	private static Map<String, BusinessType> businessTypeMap = new HashMap<String, BusinessType>();

	private StandardTypeProcessor standardTypeProcessor;

	public StandardTypeProcessor getStandardTypeProcessor() {
		return standardTypeProcessor;
	}

	public void setStandardTypeProcessor(
			StandardTypeProcessor standardDataTypeProcessor) {
		this.standardTypeProcessor = standardDataTypeProcessor;
	}

	public String getType(String id, String language) {
		BusinessType type = getBusinessTypes(id);
		String stdType = standardTypeProcessor.getType(type.getTypeId(), language);
		String result = MetadataUtil.formatType(stdType,
				type.getPlaceholderValueList());
		if (result != null) {
			return result;
		}
		throw new RuntimeException(
				String.format("找不到ID:[%s]对应的业务数据类型所属标准数据类型中的语言类型:[%s]。", id,language));
	}

	public void addBusinessTypes(BusinessTypes businessTypes) {
		if(businessTypes!=null&&businessTypes.getBusinessTypeList()!=null){
			for(BusinessType type:businessTypes.getBusinessTypeList()){
				businessTypeMap.put(type.getId(), type);
			}
		}
	}

	public BusinessType getBusinessTypes(String id) {
		if(businessTypeMap.containsKey(id)){
			return businessTypeMap.get(id);
		}
		throw new RuntimeException(
		String.format("找不到ID:[%s]对应的业务数据类型。", id));
	}

	public void removeBusinessTypes(BusinessTypes businessTypes) {
		if(businessTypes!=null&&businessTypes.getBusinessTypeList()!=null){
			for(BusinessType type:businessTypes.getBusinessTypeList()){
				businessTypeMap.remove(type.getId());
			}
		}
	}

//	public String getType(String packageName, String name, String language) {
//		if (packageName != null && businessTypeMap.containsKey(packageName)) {
//			BusinessType businessType = businessTypeMap.get(packageName).get(
//					name);
//			String type = standardTypeProcessor.getType(packageName,
//					businessType.getType(), language);
//			String result = MetadataUtil.formatType(type,
//					businessType.getPlaceholderValueList());
//			if (result != null) {
//				return result;
//			}
//		}
//		for (String pkgName : businessTypeMap.keySet()) {
//			BusinessType businessType = businessTypeMap.get(pkgName).get(name);
//			String type = standardTypeProcessor.getType(packageName,
//					businessType.getType(), language);
//			String result = MetadataUtil.formatType(type,
//					businessType.getPlaceholderValueList());
//			if (result != null) {
//				return result;
//			}
//		}
//		throw new RuntimeException(
//				String.format("找不到包名：[%s], 类型[%s], 语言[%s]对应的类型。", packageName,
//						name, language));
//	}
//
//	public String getType(String name, String language) {
//		return getType(null, name, language);
//	}
//
//	public void addBusinessTypes(BusinessTypes businessTypes) {
//		Map<String, BusinessType> typeMap = new HashMap<String, BusinessType>();
//		businessTypeMap.put(
//				MetadataUtil.passNull(businessTypes.getPackageName()), typeMap);
//		if (businessTypes.getBusinessTypeList() != null) {
//			for (BusinessType businessType : businessTypes
//					.getBusinessTypeList()) {
//				typeMap.put(businessType.getName(), businessType);
//			}
//		}
//	}
}
