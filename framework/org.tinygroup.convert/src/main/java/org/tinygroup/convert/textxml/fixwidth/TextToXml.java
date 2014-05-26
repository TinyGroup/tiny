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
import org.tinygroup.convert.XmlUtils;
import org.tinygroup.convert.text.TextBaseParse;
import org.tinygroup.convert.text.config.Text;
import org.tinygroup.convert.text.config.TextCell;
import org.tinygroup.convert.text.config.TextRow;

import java.util.List;
import java.util.Map;

public class TextToXml extends TextBaseParse implements Converter<String, String> {
	private String rootNodeName;
	private String rowNodeName;
	private String lineSplit;

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
	public TextToXml(Map<String, String> titleMap, String rootNodeName,
			String rowNodeName, String lineSplit) {
		this.rootNodeName = rootNodeName;
		this.rowNodeName = rowNodeName;
		this.lineSplit = lineSplit;
		setTitleMap(titleMap);
	}

	public String convert(String inputData) throws ConvertException {
		Text text = computeFixWidthText(inputData, lineSplit);
		StringBuffer sb = new StringBuffer();
		XmlUtils.appendHeader(sb, rootNodeName);
		List<TextRow> rows = text.getRows();
		List<String> nodeTags = getFisrtRowRealNames(text);
		for (int i = 1; i < rows.size(); i++) {
			TextRow row = rows.get(i);
			List<TextCell> cells = row.getCells();
			XmlUtils.appendHeader(sb, rowNodeName);
			for(int j = 0;j< nodeTags.size() ; j ++ ){
				XmlUtils.appendHeader(sb, nodeTags.get(j));
				sb.append(cells.get(j).getValue());
				XmlUtils.appendFooter(sb, nodeTags.get(j));
			}
			XmlUtils.appendFooter(sb, rowNodeName);
		}
		XmlUtils.appendFooter(sb, rootNodeName);
		
		
		return sb.toString();
	}

}
