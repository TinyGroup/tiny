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
package org.tinygroup.weblayer.webcontext.session.serializer.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.convert.objectjson.fastjson.JsonToObject;
import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;
import org.tinygroup.weblayer.webcontext.session.serializer.Serializer;

import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonConvertSerializer implements Serializer {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void serialize(Object objectToEncode, OutputStream os)
			throws Exception {

		ObjectToJson objectToXml = new ObjectToJson(SerializerFeature.WriteClassName);
		String result = objectToXml.convert(objectToEncode);
		os.write(result.getBytes("UTF-8"));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object deserialize(InputStream is) throws Exception {
		JsonToObject jsonToObject = new JsonToObject(Map.class);
		Map<String, Object> attrs =(Map<String, Object>) jsonToObject
				.convert(StreamUtil.readText(is, "UTF-8", false));
		return attrs;
	}

	
	public String toString() {
		return getClass().getSimpleName();
	}
}
