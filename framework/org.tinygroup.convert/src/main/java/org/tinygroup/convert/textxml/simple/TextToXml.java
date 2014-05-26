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
import org.tinygroup.convert.XmlUtils;

import java.util.Map;

public class TextToXml implements Converter<String, String> {
	private String rootNodeName;
	private String rowNodeName;
	private String lineSplit;
	private String fieldSplit;
	private Map<String, String> titleMap;

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
	public TextToXml(Map<String, String> titleMap, String rootNodeName,
			String rowNodeName, String lineSplit, String fieldSplit) {
		this.rootNodeName = rootNodeName;
		this.rowNodeName = rowNodeName;
		this.lineSplit = lineSplit;
		this.fieldSplit = fieldSplit;
		this.titleMap = titleMap;
	}

	public String convert(String inputData) throws ConvertException {
		String[] lines = inputData.split(lineSplit);
		String[] fieldNames = lines[0].split(fieldSplit);
		StringBuffer sb = new StringBuffer();
		XmlUtils.appendHeader(sb, rootNodeName);
		for (int i = 1; i < lines.length; i++) {
			String[] values = lines[i].split(fieldSplit);
			checkFeidlCount(fieldNames, i, values);
			XmlUtils.appendHeader(sb, rowNodeName);
			for (int j = 0; j < fieldNames.length; j++) {
				XmlUtils.appendHeader(sb, titleMap.get(fieldNames[j]));
				sb.append(values[j]);
				XmlUtils.appendFooter(sb, titleMap.get(fieldNames[j]));
			}
			XmlUtils.appendFooter(sb, rowNodeName);
		}
		XmlUtils.appendFooter(sb, rootNodeName);
		return sb.toString();
	}

	private void checkFeidlCount(String[] fieldNames, int i, String[] values) throws ConvertException {
		if (fieldNames.length != values.length) {
			throw new ConvertException("标题个数(" + fieldNames.length + ")与第【" + i
					+ "】行的数据个数(" + values.length + ")不相等");
		}
	}

}
