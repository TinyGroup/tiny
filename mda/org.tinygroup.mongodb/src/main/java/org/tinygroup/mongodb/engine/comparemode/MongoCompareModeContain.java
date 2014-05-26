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
package org.tinygroup.mongodb.engine.comparemode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.springutil.SpringUtil;

/**
 * 
 * 功能说明: comparemode的容器类

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-27 <br>
 * <br>
 */
public class MongoCompareModeContain {

	public static final String COMPARE_MODE_CONTAIN="mongoCompareModeContain";
	
	private Map<String, MongoCompareMode> compareModes=new HashMap<String, MongoCompareMode>();
	
	public MongoCompareModeContain(){
		 Collection<MongoCompareMode> mongoCompareModes=SpringUtil.getBeansOfType(MongoCompareMode.class);
		 if(!CollectionUtil.isEmpty(mongoCompareModes)){
			 for (MongoCompareMode compareMode : mongoCompareModes) {
				 compareModes.put(compareMode.getCompareKey(), compareMode);
			}
		 }
	}
	
	public MongoCompareMode getCompareMode(String compareModeName){
		return compareModes.get(compareModeName);
	}
	
	public void addCompareMode(String compareModeName,MongoCompareMode compareMode){
		compareModes.put(compareModeName, compareMode);
	}
	
}
