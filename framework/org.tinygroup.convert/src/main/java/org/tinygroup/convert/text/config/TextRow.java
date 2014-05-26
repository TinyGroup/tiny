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

public class TextRow {
	private List<TextCell> cells = new ArrayList<TextCell>();

	public List<TextCell> getCells() {
		return cells;
	}

	public void setCells(List<TextCell> cells) {
		this.cells = cells;
	}

	public void addCell(TextCell cell) {
		cells.add(cell);
	}

	public String toString(String cellSplit, String rowSplit,boolean checkLength) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0 ; i < cells.size() - 1 ; i ++ ){
			buffer.append(cells.get(i).toString(checkLength)).append(cellSplit);
		}
		buffer.append(cells.get(cells.size()-1).toString(checkLength)).append(rowSplit);
		return buffer.toString();
	}
	
	public void adjustLength(List<Integer> lengths){
		for(int i = 0 ; i < cells.size();i++){
			cells.get(i).setLength(lengths.get(i));
		}
	}

}
