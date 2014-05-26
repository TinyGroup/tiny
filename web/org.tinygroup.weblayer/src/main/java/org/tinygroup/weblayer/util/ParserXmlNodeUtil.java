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
package org.tinygroup.weblayer.util;

import java.beans.PropertyEditor;
import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.webcontext.parser.util.BeanWrapperImpl;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明: 解析xmlnode的工具类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-6 <br>
 * <br>
 */
public class ParserXmlNodeUtil {

	// 属性名称。对应为springbean 的名称
	private static final String BEAN_NAME = "bean-name";
	private static BeanWrapperImpl beanWrapper=new BeanWrapperImpl();
	static{
		Map customEditors=SpringUtil.getCustomEditors();
		Set keySet=customEditors.keySet();
		for (Object key : keySet) {
			Class requiredType=(Class)key;
			if(customEditors.get(requiredType) instanceof Class){
				try {
					beanWrapper.registerCustomEditor(requiredType, (PropertyEditor) ((Class)customEditors.get(requiredType)).newInstance());
				} catch (Exception e) {
					throw new RuntimeException("注册客户自定义类型转换出错",e);
				} 
			}
			if(customEditors.get(requiredType) instanceof PropertyEditor){
				beanWrapper.registerCustomEditor(requiredType, (PropertyEditor) customEditors.get(requiredType));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] parseConfigToArray(String subNodeName, XmlNode node,
			Class<T> clazz) {
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
		if (subNodeName != null) {
			List<XmlNode> subNodes = nameFilter.findNodeList(subNodeName);
			if (!CollectionUtil.isEmpty(subNodes)) {
				T[] array = (T[]) Array.newInstance(clazz, subNodes.size());
				for (int i = 0; i < subNodes.size(); i++) {
					T object = newInstance(
							subNodes.get(i).getAttribute(BEAN_NAME), clazz);
					array[i] = object;
				}
				return array;

			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public static <T> T[] parseConfigToArray(String subNodeName, XmlNode node,
			Class<T> clazz, String... attributeNames) {
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
		if (subNodeName != null) {
			List<XmlNode> subNodes = nameFilter.findNodeList(subNodeName);
			if (!CollectionUtil.isEmpty(subNodes)) {
				T[] array = (T[]) Array.newInstance(clazz, subNodes.size());
				for (int i = 0; i < subNodes.size(); i++) {
					array[i] = parseConfigToObject(null,null,
							subNodes.get(i), clazz, attributeNames);
				}
				return array;

			}
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] parseConfigToArray(String subNodeName,String propertyNode, XmlNode node,
			Class<T> clazz) {
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
		if (subNodeName != null) {
			List<XmlNode> subNodes = nameFilter.findNodeList(subNodeName);
			if (!CollectionUtil.isEmpty(subNodes)) {
				T[] array = (T[]) Array.newInstance(clazz, subNodes.size());
				for (int i = 0; i < subNodes.size(); i++) {
					XmlNode xmlNode=subNodes.get(i);
					array[i] = parseConfigToObject(null,propertyNode,xmlNode, clazz);
				}
				return array;

			}
		}
		return null;

	}

	public static <T> T parseConfigToObject(String subNodeName, XmlNode node,
			Class<T> clazz) {
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		XmlNode xmlNode=node;
		if (subNodeName != null) {
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
			XmlNode subNode = nameFilter.findNode(subNodeName);
			if (!ObjectUtil.isEmptyObject(subNode)) {
				xmlNode=subNode;
			}
		} 
		return newInstance(xmlNode.getAttribute(BEAN_NAME), clazz);
	}

	public static <T> T parseConfigToObject(String subNodeName,String propertyNode, XmlNode node,
			Class<T> clazz, String... attributeNames) {
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		XmlNode xmlNode=node;
		if (subNodeName != null) {
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
			XmlNode subNode = nameFilter.findNode(subNodeName);
			if (!ObjectUtil.isEmptyObject(subNode)) {
				xmlNode=subNode;
			}
		} 
		if(propertyNode!=null){
			return createObjectWithProperty(clazz, xmlNode, propertyNode);
		}
		return createObject(clazz, xmlNode, attributeNames);
	}

	private static <T> T createObject(Class<T> clazz, XmlNode subNode,
			String... attributeNames) {
		T object = newInstance(subNode.getAttribute(BEAN_NAME), clazz);
		Map<String, String> properties=CollectionUtil.createHashMap();
		for (String attribute : attributeNames) {
			try {
				String value = subNode.getAttribute(attribute);
				if (value == null) {
					NameFilter<XmlNode> nameFilter2 = new NameFilter<XmlNode>(
							subNode);
					List<XmlNode> valueNodes = nameFilter2
							.findNodeList(attribute);
					StringBuffer buffer=new StringBuffer();
					if (!CollectionUtil.isEmpty(valueNodes)) {
						for (int j = 0; j < valueNodes.size(); j++) {
							buffer.append(valueNodes.get(j).getContent());
							if(j!=valueNodes.size()-1){
								buffer.append(",");
							}
						}
						value=buffer.toString();
					}
				}
				properties.put(attribute, value);
			} catch (Exception e) {
				throw new RuntimeException("设置对象属性出错", e);
			}
		}
		return setAttribute(object, properties);
	}
	/**
	 * 
	 * propertyNode 属性节点名，该节点必须有name属性，value可以有，没有设置value则取节点文本值
	 * @param clazz
	 * @param node
	 * @param propertyNode
	 * @return
	 */
	private static <T> T createObjectWithProperty(Class<T> clazz, XmlNode node,
			String propertyNode) {
		T object = newInstance(node.getAttribute(BEAN_NAME), clazz);
		Map<String, String> properties=CollectionUtil.createHashMap();
		NameFilter<XmlNode> propertyFilter = new NameFilter<XmlNode>(node);
		List<XmlNode> subNodes=propertyFilter.findNodeList(propertyNode);
		for (XmlNode subNode : subNodes) {
			String value=subNode.getAttribute("value");
			if(value==null){
				value=subNode.getContent();
			}
			properties.put(subNode.getAttribute("name"),value);
		}
		
		return setAttribute(object, properties);
	}
	
	private static <T> T setAttribute(T object,
			Map<String,String> properties) {
	     beanWrapper.setWrappedInstance(object);
		for (String attribute : properties.keySet()) {
			try {
				String value = properties.get(attribute);
				beanWrapper.setPropertyValue(attribute, value);
			} catch (Exception e) {
				throw new RuntimeException("设置对象属性出错", e);
			}
		}
		if (object instanceof InitializingBean) {
			try {
				((InitializingBean) object).afterPropertiesSet();
			} catch (Exception e) {
				throw new RuntimeException("initializingBean error", e);
			}
		}
		return object;
	}

	public static <T> Map<String, T> parseConfigToMap(String subNodeName,
			String attributeKeyName, XmlNode node, Class<T> clazz) {
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		Assert.assertNotNull(attributeKeyName, "解析的节点属性名不能为空");
		if (subNodeName != null) {
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
			List<XmlNode> subNodes = nameFilter.findNodeList(subNodeName);
			if (!CollectionUtil.isEmpty(subNodes)) {
				Map<String, T> map = new LinkedHashMap<String, T>();
				for (XmlNode subNode : subNodes) {
					T object = newInstance(subNode.getAttribute(BEAN_NAME),
							clazz);
					map.put(subNode.getAttribute(attributeKeyName), object);
				}
				return map;
			}
		}
		return null;
	}

	public static Map<String, String> parseConfigToMap(String subNodeName,
			String attributeKeyName, String attributeValueName, XmlNode node) {
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		Assert.assertNotNull(attributeKeyName, "解析的节点属性名不能为空");
		Assert.assertNotNull(attributeValueName, "解析的节点属性名不能为空");
		if (subNodeName != null) {
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
			List<XmlNode> subNodes = nameFilter.findNodeList(subNodeName);
			Map<String, String> map = new ConcurrentHashMap<String, String>();
			if (!CollectionUtil.isEmpty(subNodes)) {
				for (XmlNode subNode : subNodes) {
					map.put(subNode.getAttribute(attributeKeyName),
							subNode.getAttribute(attributeValueName));
				}
			}
			return map;
		}
		return null;
	}

	public static String getAttributeValueWithSubNode(String subNodeName,
			String attribute, XmlNode xmlNode) {
		Assert.assertNotNull(xmlNode, "解析的节点对象不能为空");
		Assert.assertNotNull(attribute, "解析的节点属性名不能为空");
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		XmlNode node = xmlNode;
		if (subNodeName != null) {
			node = nameFilter.findNode(subNodeName);
		}
		if (!ObjectUtil.isEmptyObject(node)) {
			return node.getAttribute(attribute);
		}
		return null;

	}

	public static String getAttributeValue(String attribute, XmlNode node) {
		return getAttributeValueWithSubNode(null, attribute, node);

	}
	
	/**
	 * 
	 * 是否存在指定节点名称
	 * @param nodeName
	 * @param node
	 * @return
	 */
	public static boolean existNode(String nodeName,XmlNode node){
		Assert.assertNotNull(node, "解析的节点对象不能为空");
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
		return  nameFilter.findNode(nodeName)!=null;
	}

	/**
	 * 
	 * 根据beanName和类型获取实例
	 * 
	 * @param bean
	 * @param clazz
	 * @return
	 */
	private static <T> T newInstance(String bean, Class<T> clazz) {
		T instance = null;
		try {
			instance = SpringUtil.getBean(bean, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		if (instance == null) {
			try {
				instance = clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return instance;
	}

}