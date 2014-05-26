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
package org.tinygroup.convert.textxml.fixwidth;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;
import org.tinygroup.convert.text.TextBaseParse;
import org.tinygroup.convert.text.config.Text;
import org.tinygroup.convert.text.config.TextCell;
import org.tinygroup.convert.text.config.TextRow;
import org.tinygroup.convert.util.ConvertUtil;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlToText extends TextBaseParse implements
		Converter<String, String> {
	private String rootNodeName;
	private String rowNodeName;
	private String lineSplit;
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
	 */
	public XmlToText(Map<String, String> titleMap, List<String> fieldList,
			String rootNodeName, String rowNodeName, String lineSplit) {
		this.rootNodeName = rootNodeName;
		this.rowNodeName = rowNodeName;
		this.lineSplit = lineSplit;
		this.fieldList = fieldList;
        setTitleMap(titleMap);
    }

	private String getNodeValue(XmlNode node){
		if(node==null) {
            return "";
        }
		return node.getContent();
	}
	/**
 * 
 */
	public String convert(String inputData) throws ConvertException {
		XmlNode root = new XmlStringParser().parse(inputData).getRoot();
		checkRootNodeName(root);
		List<XmlNode> rowList = root.getSubNodes(rowNodeName);
		Text text = new Text();
		
		List<Integer> rowMexLengths = new ArrayList<Integer>();
		TextRow titleRow = new TextRow();
		for(int i  = 0;i< fieldList.size();i++ ){
			String title = getProperty(fieldList.get(i));
			rowMexLengths.add(ConvertUtil.getBytesLength(title));
			TextCell titleCell  = new TextCell(title);
			titleRow.addCell(titleCell);
		}
		text.addRow(titleRow);
		
		for(int i = 0 ; i < rowList.size() ; i ++ ){
			TextRow dataRow = new TextRow();
			XmlNode rowNode = rowList.get(i);
			for (int j = 0; j < fieldList.size(); j++) {
				int maxLength = rowMexLengths.get(j);
				String fieldName = fieldList.get(j);
				XmlNode fieldNode = rowNode.getSubNode(fieldName);
				String fieldValue = getNodeValue(fieldNode);
				int fieldLength =ConvertUtil.getBytesLength(fieldValue);
				if(fieldLength>maxLength){
					rowMexLengths.remove(j);
					rowMexLengths.add(j, fieldLength);
				}
				TextCell fieldCell = new TextCell(fieldValue);
				dataRow.addCell(fieldCell);
			}
			text.addRow(dataRow);
		}
		text.adjustLength(rowMexLengths);
		return text.toString(" ", lineSplit, true);
	}
	

	

	private void checkRootNodeName(XmlNode root) throws ConvertException {
		if (root.getNodeName() == null
				|| !root.getNodeName().equals(rootNodeName)) {
			throw new ConvertException("根节点名称[" + root.getRoot().getNodeName()
					+ "]与期望的根节点名称[" + rootNodeName + "]不一致！");
		}
	}
}
