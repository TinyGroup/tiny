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
package org.tinygroup.dict.impl;

import org.tinygroup.context.Context;
import org.tinygroup.dict.Dict;
import org.tinygroup.dict.DictFilter;
import org.tinygroup.dict.DictLoader;
import org.tinygroup.dict.DictManager;

public abstract class AbstractDictLoader implements DictLoader {

	private DictFilter dictFilter;

	private String groupName;
	private String language;

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public Dict getDict(String dictTypeName, DictManager dictManager,
			Context context) {
		try {
			Dict dict = (Dict) dictManager.getCache().get(getGroupName(),
					dictTypeName);
			if (dict != null && dictFilter != null) {
				dict = dictFilter.filte(context, dict);
			}
			return dict;
		} catch (Exception e) {
            //找不到返回空
			return null;
		}

	}

	public void setDictFilter(DictFilter dictFilter) {
		this.dictFilter = dictFilter;
	}

	protected void putDict(String name, Dict dict, DictManager dictManager) {
		dictManager.getCache().put(this.getGroupName(), name, dict);
	}

	public void clear(DictManager dictManager) {
		dictManager.getCache().cleanGroup(getGroupName());
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;

	}

}
