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
package org.tinygroup.convert.objectxml.simple;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;
import org.tinygroup.convert.XmlUtils;

import java.lang.reflect.Field;
import java.util.List;

public class ObjectToXml<T> implements Converter<List<T>, String> {
	private boolean fieldAsAttribute;
	private String rootNodeName;
	private String rowNodeName;

	public ObjectToXml(boolean fieldAsAttribute, String rootNodeName,
			String rowNodeName) {
		this.fieldAsAttribute = fieldAsAttribute;
		this.rootNodeName = rootNodeName;
		this.rowNodeName = rowNodeName;
	}

	public String convert(List<T> inputData) throws ConvertException {
		StringBuffer sb = new StringBuffer();
		XmlUtils.appendHeader(sb, rootNodeName);
		if (inputData != null) {
			for (T object : inputData) {
				if (fieldAsAttribute) {
					sb.append("<");
					sb.append(rowNodeName);
					sb.append(" ");
					for (Field field : object.getClass().getDeclaredFields()) {
						Object value = getAttributeValue(object, field);
						if (value != null) {
							sb.append(" ");
							sb.append(field.getName());
							sb.append("=\"");
							sb.append(value.toString());
							sb.append("\"");
						}
					}
					sb.append("/>");
				} else {
					for (Field field : object.getClass().getDeclaredFields()) {
						Object value = getAttributeValue(object, field);
						if (value != null) {
							XmlUtils.appendHeader(sb, rowNodeName);
							sb.append(value.toString());
							XmlUtils.appendFooter(sb, rowNodeName);
						}
					}
				}
			}
		}
		XmlUtils.appendFooter(sb, rootNodeName);
		return sb.toString();
	}

	private String getAttributeValue(T object, Field field) throws ConvertException {
		try {
			Object value = BeanUtils.getProperty(object, field.getName());
			if (value == null) {
				return "";
			}
			return value.toString();
		} catch (Exception e) {
			throw new ConvertException(e);
		}
	}
}
