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
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 功能说明: 不支持对象嵌套这种情况

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-26 <br>
 * <br>
 */
public class XmlToObject<T> implements Converter<String, List<T>> {
	private String className;
	private String rootNodeName;
	private String rowNodeName;

	public XmlToObject(String className, String rootNodeName, String rowNodeName) {
		this.className = className;
		this.rootNodeName = rootNodeName;
		this.rowNodeName = rowNodeName;
	}

	public List<T> convert(String inputData) throws ConvertException {
		XmlNode root = new XmlStringParser().parse(inputData).getRoot();
		checkRootNodeName(root);
		List<T> list = new ArrayList<T>();
		List<XmlNode> rowList = root.getSubNodes(rowNodeName);
		for (XmlNode node : rowList) {
			T object = newObject();
			setProperty(object, node);
			list.add(object);
		}
		return list;
	}

	private void setProperty(T object, XmlNode node) throws ConvertException {
		for (Field field : object.getClass().getDeclaredFields()) {
			String value = getValue(node, field.getName());
			if (value != null) {
				setPropertyValue(object, field, value);
			}
		}
	}

	private void setPropertyValue(T object, Field field, String value) throws ConvertException {
		try {
			BeanUtils.setProperty(object, field.getName(), value);
		} catch (Exception e) {
			throw new ConvertException(e);
		}
	}

	private String getValue(XmlNode node, String name) {
		String attribute = node.getAttribute(name);
		if (attribute != null) {
			return attribute;
		}
		XmlNode subNode = node.getSubNode(name);
		if (subNode != null) {
			return subNode.getContent();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private T newObject() throws ConvertException {
		try {
			return (T) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new ConvertException(e);
		}
	}

	private void checkRootNodeName(XmlNode root) throws ConvertException {
		if (root.getNodeName() == null
				|| !root.getNodeName().equals(rootNodeName)) {
			throw new ConvertException("根节点名称[" + root.getRoot().getNodeName()
					+ "]与期望的根节点名称[" + rootNodeName + "]不一致！");
		}
	}
}
