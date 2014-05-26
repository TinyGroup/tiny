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
package org.tinygroup.dict;

import org.tinygroup.context.Context;

public class SimpleDictFilter implements DictFilter {

	public Dict filte(Context context, Dict dict) {
		if (context == null) {
			return dict;
		}
		String lang = context.get("LANG");
		if (lang == null) {
			return dict;
		}
		Dict newDict = new Dict();
		newDict.setName(dict.getName());
		newDict.setDescription(dict.getDescription());
		for (DictGroup dictGroup : dict.getDictGroupList()) {
			if (dictGroup.getName().equals(lang)) {
				newDict.addDictGroup(dictGroup);
				return newDict;
			}
		}
		throw new RuntimeException("不能在<" + dict.getName() + ">中找到分组<" + lang
				+ ">");
	}

}
