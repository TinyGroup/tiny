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
package org.tinygroup.convert.objectjson.jackson;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import java.io.ByteArrayOutputStream;

public class ObjectToJson<T> implements Converter<T, String> {
    // can reuse, share globally
	private ObjectMapper mapper = new ObjectMapper();

	public ObjectToJson() {
		mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
	}

	public ObjectToJson(Inclusion inclusion) {
		mapper.setSerializationInclusion(inclusion);
	}

	public String convert(T object) throws ConvertException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			mapper.writeValue(outputStream, object);
			return outputStream.toString("UTF-8");
		} catch (Exception e) {
			throw new ConvertException(e);
		}
	}

}
