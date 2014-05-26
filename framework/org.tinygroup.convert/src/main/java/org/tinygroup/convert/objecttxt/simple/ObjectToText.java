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
package org.tinygroup.convert.objecttxt.simple;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;
import org.tinygroup.convert.text.config.Text;
import org.tinygroup.convert.text.config.TextCell;
import org.tinygroup.convert.text.config.TextRow;
import org.tinygroup.convert.util.ConvertUtil;

import java.util.List;
import java.util.Map;

public class ObjectToText<T> implements Converter<List<T>, String> {
	private Map<String, String> titleMap;
	private List<String> properties;
	private String lineSplit;
	private String fieldSplit;

	public ObjectToText(Map<String, String> titleMap, List<String> properties,
			String lineSplit, String fieldSplit) {
		this.titleMap = titleMap;
		this.properties = properties;
		this.lineSplit = lineSplit;
		this.fieldSplit = fieldSplit;
	}

	public String convert(List<T> objectList) throws ConvertException {
		Text text = new Text();
		TextRow titleRow = computeTitleRow();
		text.addRow(titleRow);
		for(int i=0;i<objectList.size();i++){
			text.addRow(computeDataRow(objectList.get(i)));
		}
		return text.toString(fieldSplit,lineSplit,false);
	}
	
	private TextRow computeTitleRow(){
		TextRow row = new TextRow();
		for (int i = 0; i < properties.size() ; i++) {
			String title = getTitle(properties.get(i));
			TextCell cell = new TextCell(title);
			row.addCell(cell);
		}
		return row;
	}


	private String getTitle(String property) {
		String title = titleMap.get(property);
		if (title == null || "".equals(title.trim())) {
			return property;
		}
		return title;
	}

	private TextRow computeDataRow(T object) throws ConvertException {
		TextRow row = new TextRow();
		for (int i = 0; i < properties.size() ; i++) {
			String property = properties.get(i);
			TextCell cell = new TextCell(getAttributeValue(object, property));
			row.addCell(cell);
		}
		return row;
	}
	
	
	private String getAttributeValue(T object, String property) throws ConvertException {
		return ConvertUtil.getAttributeValue(object, property);
	}

}
