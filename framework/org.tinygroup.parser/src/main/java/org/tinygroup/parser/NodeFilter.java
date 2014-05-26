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
package org.tinygroup.parser;

import java.util.Map;
import java.util.List;

/**
 * 节点过滤器接口 为提升过滤效率，过滤器设计为有状态的， 需要被过滤器处理的节点一般都过实现类的构造函数进行配置，并进行预处理
 * 
 * @author luoguo
 * 
 */
public interface NodeFilter<T extends Node<T>> {
	/**
	 * 初始化节点
	 * 
	 * @param node
	 */
	void init(T node);

	/**
	 * 设置必须包含的属性及对应属性的值，必须存在
	 * 
	 * @param includeAttributes
	 */
    NodeFilter setIncludeAttribute(Map<String, String> includeAttributes);

	/**
	 * 设置必须包含的属性及对应的属性的值，必须存在
	 * 
	 * @param key
	 * @param value
	 */
    NodeFilter setIncludeAttribute(String key, String value);

	/**
	 * 设置必须包含的属性
	 * 
	 * @param includeAttribute
	 */
    NodeFilter setIncludeAttributes(String... includeAttribute);

	/**
	 * 设置必须排除的属性及对应属性值 如果包含属性，但属性的值与Map中不相同，允许存在该属性 若包含属性且属性的值与Map中相同，则不允许存在该属性
	 * 
	 * @param excludeAttribute
	 */
    NodeFilter setExcludeAttribute(Map<String, String> excludeAttribute);

	/**
	 * 设置必须排除的属性，指定的属性不能存在
	 * 
	 * @param excludeAttribute
	 */
    NodeFilter setExcludeAttribute(String... excludeAttribute);

	/**
	 * 设置必须包含的内容，只需要context中包include该值就行
	 * 
	 * @param includeText
	 */
    NodeFilter setIncludeText(String... includeText);

	/**
	 * 设置必须排除的内容
	 * 
	 * @param excludeText
	 */
    NodeFilter setExcludeText(String... excludeText);

	/**
	 * 设置必须包含的子节点
	 * 
	 * @param includeNode
	 */
    NodeFilter setIncludeNode(String... includeNode);

	/**
	 * 设置父节点不允许的节点名称
	 * 
	 * @param excludeByNode
	 */

    NodeFilter setExcludeByNode(String... excludeByNode);

	/**
	 * 设置父节点必须包含的节点名称
	 * 
	 * @param includeByNode
	 */
    NodeFilter setIncludeByNode(String... includeByNode);

	/**
	 * 设置必须排除的子节点
	 * 
	 * @param excludeNode
	 */

    NodeFilter setExcludeNode(String... excludeNode);

	/**
	 * 设置至少包含一个指定名称的节点
	 * 
	 * @param xorSubNode
	 */
    NodeFilter setXorSubNode(String... xorSubNode);

	/**
	 * 设置至少包含一个指定名称属性
	 * 
	 * @param xorProperties
	 */
    NodeFilter setXorProperties(String... xorProperties);

	/**
	 * 清除过滤条件
	 */
    NodeFilter clearCondition();

	/**
	 * 设置要搜索的节点名称
	 */
    NodeFilter setNodeName(String nodeName);

	/**
	 * 查找指定节点名称及满足其他条件的节点列表
	 * 
	 * @param nodeName
	 * @return
	 */
	List<T> findNodeList(String nodeName);

	/**
	 * 根据名字及其他条件查找节点，如果有多个，也只返回第一个
	 * 
	 * @param nodeName
	 *            要查找的节点名称
	 * @return
	 */
	T findNode(String nodeName);

	/**
	 * 搜索符合设置的节点名称的节点，如果有多个，则只返回找到的第一个
	 * 
	 * @return
	 */
	T findNode();

	/**
	 * 搜索符合设置的节点名称的节点列表
	 * 
	 * @return
	 */
	List<T> findNodeList();
}
