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
package org.tinygroup.convert.textxml.simple;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.util.List;
import java.util.Map;

public class XmlToText implements Converter<String, String> {
	private String rootNodeName;
	private String rowNodeName;
	private String lineSplit;
	private String fieldSplit;
	private Map<String, String> titleMap;
	private List<String> fieldList;

	/**
	 * 文本转换为Xml
	 * 
	 * @param rootNodeName
	 *            根节点名称
	 * @param rowNodeName
	 *            行节点名称
	 * @param lineSplit
	 *            行分隔附
	 * @param fieldSplit
	 *            字段分隔符
	 */
	public XmlToText(Map<String, String> titleMap, List<String> fieldList,
			String rootNodeName, String rowNodeName, String lineSplit,
			String fieldSplit) {
		this.rootNodeName = rootNodeName;
		this.rowNodeName = rowNodeName;
		this.lineSplit = lineSplit;
		this.fieldSplit = fieldSplit;
		this.titleMap = titleMap;
		this.fieldList = fieldList;
	}

	/**
 * 
 */
	public String convert(String inputData) throws ConvertException {
		XmlNode root = new XmlStringParser().parse(inputData).getRoot();
		checkRootNodeName(root);
		List<XmlNode> rowList = root.getSubNodes(rowNodeName);
		StringBuffer sb = new StringBuffer();
		if (rowList.size() > 0) {
			for (int i = 0; i < fieldList.size(); i++) {
				if (i > 0) {
					sb.append(fieldSplit);
				}
				sb.append(titleMap.get(fieldList.get(i)));
			}
			sb.append(lineSplit);
            // 对所有的行进行处理
			for (int row = 0; row < rowList.size(); row++) {
				XmlNode rowNode = rowList.get(row);
				for (int i = 0; i < fieldList.size(); i++) {
					XmlNode fieldNode = rowNode.getSubNode(fieldList.get(i));
					if (i > 0) {
						sb.append(fieldSplit);
					}
					if (fieldNode != null) {
						sb.append(fieldNode.getContent());
					}
				}
				if (row < rowList.size() - 1) {
					sb.append(lineSplit);
				}
			}

		}
		return sb.toString();
	}

	private void checkRootNodeName(XmlNode root) throws ConvertException {
		if (root.getNodeName() == null
				|| !root.getNodeName().equals(rootNodeName)) {
			throw new ConvertException("根节点名称[" + root.getRoot().getNodeName()
					+ "]与期望的根节点名称[" + rootNodeName + "]不一致！");
		}
	}
}
