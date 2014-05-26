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
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

public class JsonToObject<T> implements Converter<String, T> {
    private String encode="UTF-8";
    // can reuse, share globally
    private ObjectMapper mapper = new ObjectMapper();
	private Class<T> rootClass;
    public JsonToObject(Class<T> rootClass,String encode) {
        this.rootClass = rootClass;
        this.encode=encode;
    }
	public JsonToObject(Class<T> rootClass) {
		this.rootClass = rootClass;
	}

	public T convert(String inputData) throws ConvertException {
		try {
			return mapper.readValue(inputData.getBytes(encode), rootClass);
		} catch (Exception e) {
			throw new ConvertException(e);
		}
	}

}
