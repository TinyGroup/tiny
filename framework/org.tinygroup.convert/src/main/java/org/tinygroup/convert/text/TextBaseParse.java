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
package org.tinygroup.convert.text;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.text.config.Text;
import org.tinygroup.convert.text.config.TextCell;
import org.tinygroup.convert.text.config.TextRow;
import org.tinygroup.convert.util.ConvertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextBaseParse {

	private Map<String, String> titleMap;

    public Map<String, String> getTitleMap() {
        return titleMap;
    }

    public void setTitleMap(Map<String, String> titleMap) {
        this.titleMap = titleMap;
    }

    protected TextRow computeDataRow(List<String> values) {
		TextRow dataRow = new TextRow();
		for (int j = 0; j < values.size(); j++) {
			TextCell cell = new TextCell(values.get(j).trim());
			dataRow.addCell(cell);
		}
		return dataRow;
	}

	protected TextRow computeTitleRow(List<String> titles) {
		TextRow titleRow = new TextRow();
		for (int i = 0; i < titles.size(); i++) {
			TextCell cell = new TextCell(getProperty(titles.get(i).trim()));
			titleRow.addCell(cell);
		}
		return titleRow;
	}

	protected List<String> getFixTitle(String string,
			List<Integer> fieldPosList, List<Integer> filedBytesPosList) throws ConvertException {
		List<String> fieldList = new ArrayList<String>();
		int start = 0;
		for (int end : fieldPosList) {
			String title = string.substring(start, end);
			filedBytesPosList.add(ConvertUtil.getBytesLength(title));
			fieldList.add(title.trim());
			start = end;
		}

		return fieldList;
	}

	protected List<String> getFixData(String string,
			List<Integer> filedBytesPosList) throws ConvertException {
		return ConvertUtil.getStringArrayByBytesLength(string,
				filedBytesPosList);
	}

	protected List<Integer> getFieldEndPos(String string) {
		List<Integer> posList = new ArrayList<Integer>();
		int start = 0;
		do {
			start = getFieldEnd(string, start);
			posList.add(start);
		} while (start < string.length());

		return posList;
	}

	protected int getFieldEnd(String string, int start) {
		int pos = start;
		while (pos < string.length() && string.charAt(pos) != ' ') {
			pos++;
		}
		while (pos < string.length() && string.charAt(pos) == ' ') {
			pos++;
		}
		return pos;
	}

	protected TextRow computeDataRow(String[] values) {
		TextRow dataRow = new TextRow();
		for (int j = 0; j < values.length; j++) {
			TextCell cell = new TextCell(values[j].trim());
			dataRow.addCell(cell);
		}
		return dataRow;
	}

	protected TextRow computeTitleRow(String[] titles) {
		TextRow titleRow = new TextRow();
		for (int i = 0; i < titles.length; i++) {
			TextCell cell = new TextCell(getProperty(titles[i].trim()));
			titleRow.addCell(cell);
		}
		return titleRow;
	}

	protected void checkFeidlCount(String[] fieldNames, int i, String[] values) throws ConvertException {
		if (fieldNames.length != values.length) {
			throw new ConvertException("标题个数(" + fieldNames.length + ")与第【" + i
					+ "】行的数据个数(" + values.length + ")不相等");
		}
	}

	protected String getProperty(String title) {
		if (titleMap == null) {
            return title;
        }
		String property = titleMap.get(title);
		if (property == null || "".equals(property.trim())) {
			return title;
		}
		return property;

	}

	public Text computeFixWidthText(String inputData, String lineSplit) throws ConvertException {
        // 总行数(包含titleRow)
		String[] lines = inputData.split(lineSplit);
        // 根据titleRow计算每列数据的位置
		List<Integer> fieldStrPosList = getFieldEndPos(lines[0]);
        // 存放每列的长度(按byte算的)
		List<Integer> filedBytesPosList = new ArrayList<Integer>();
        // 计算title
		List<String> titles = getFixTitle(lines[0], fieldStrPosList,
				filedBytesPosList);
		Text text = new Text();
		text.addRow(computeTitleRow(titles));

		for (int i = 1; i < lines.length; i++) {
			List<String> values = getFixData(lines[i], filedBytesPosList);
			text.addRow(computeDataRow(values));
		}
		return text;
	}

	public Text computeText(String inputData, String lineSplit,
			String fieldSplit) throws ConvertException {
		String[] lines = inputData.split(lineSplit);
		String[] titles = lines[0].split(fieldSplit);
		Text text = new Text();
		text.addRow(computeTitleRow(titles));

		for (int i = 1; i < lines.length; i++) {
			String[] values = lines[i].split(fieldSplit);
			checkFeidlCount(titles, i, values);
			text.addRow(computeDataRow(values));
		}
		return text;
	}

	protected List<String> getFisrtRowRealNames(Text text) {
		TextRow firstRow = text.getRows().get(0);
		List<String> names = new ArrayList<String>();
		List<TextCell> cells = firstRow.getCells();
		for (int i = 0; i < cells.size(); i++) {
			TextCell cell = cells.get(i);
			String name = getProperty(cell.getValue());
			names.add(name);
		}
		return names;
	}
}
