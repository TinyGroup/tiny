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
package org.tinygroup.convert.objectjson.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.tinygroup.convert.Converter;

/**
 * 
 * 功能说明: fastJson方式

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-15 <br>
 * <br>
 */
public class ObjectToJson<T> implements Converter<T, String> {
	
	private SerializerFeature[] features;
	
	public ObjectToJson() {
	}
	
	public ObjectToJson(SerializerFeature... features) {
		this.features=features;
	}

	public String convert(T inputData) {
		if(features!=null){
			return JSON.toJSONString(inputData, features);
		}
		return JSON.toJSONString(inputData);
	}

}
