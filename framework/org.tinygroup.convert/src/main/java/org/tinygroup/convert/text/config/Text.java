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
package org.tinygroup.convert.text.config;

import java.util.ArrayList;
import java.util.List;

public class Text {
	private List<TextRow> rows = new ArrayList<TextRow>();

	public List<TextRow> getRows() {
		return rows;
	}

	public void setRows(List<TextRow> rows) {
		this.rows = rows;
	}
	
	public void addRow(TextRow row){
		rows.add(row);
	}
	
	public String toString(String cellSplit,String rowSplit,boolean checkLength){
		StringBuffer buffer = new StringBuffer();
		for(TextRow row : rows ){
			buffer.append(row.toString(cellSplit,rowSplit,checkLength));
		}
		return buffer.toString();
	}
	
	public void adjustLength(List<Integer> lengths){
		for(TextRow row:rows){
			row.adjustLength(lengths);
		}
	}
}
