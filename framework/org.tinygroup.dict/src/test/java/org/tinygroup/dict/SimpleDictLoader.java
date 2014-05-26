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

import org.tinygroup.dict.impl.AbstractDictLoader;

public class SimpleDictLoader extends AbstractDictLoader {

	public void load(DictManager dictManager) {
		Dict dict = new Dict("Gender", "性别");
		DictGroup dictGroup = new DictGroup("CN", "中文");
		dict.addDictGroup(dictGroup);

		DictItem dictItem = new DictItem("1", "男");
		dictGroup.addDictItem(dictItem);
		dictItem = new DictItem("0", "女");
		dictGroup.addDictItem(dictItem);
		dictGroup = new DictGroup("EN", "英文");
		dict.addDictGroup(dictGroup);

		dictItem = new DictItem("1", "男");
		dictGroup.addDictItem(dictItem);
		dictItem = new DictItem("0", "女");
		dictGroup.addDictItem(dictItem);

		putDict("Gender", dict, dictManager);
	}
}
