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
package org.tinygroup.convert.objecttxt.fixwidth;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;
import org.tinygroup.convert.text.TextBaseParse;
import org.tinygroup.convert.text.config.Text;
import org.tinygroup.convert.util.ConvertUtil;

import java.util.List;
import java.util.Map;

public class TextToObject<T> extends TextBaseParse implements Converter<String, List<T>> {
	private String lineSplit;
	private Class<T> clazz;

	public TextToObject(Class<T> clazz,Map<String, String> titleMap,String lineSplit) {
		this.clazz = clazz;
		this.lineSplit = lineSplit;
		setTitleMap(titleMap);
	}

	public List<T> convert(String inputData) throws ConvertException {
		Text text = computeFixWidthText(inputData,lineSplit);
		return ConvertUtil.textToObjectList(clazz, text);
	}
	
	

	
}
