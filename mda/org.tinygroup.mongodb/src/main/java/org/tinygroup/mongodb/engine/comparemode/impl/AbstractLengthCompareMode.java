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
package org.tinygroup.mongodb.engine.comparemode.impl;

import org.bson.BSONObject;
import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.context.Context;
import org.tinygroup.mongodb.engine.MongoDbContext;
import org.tinygroup.mongodb.util.ModelUtil;

/**
 * 
 * 功能说明: 长度比较模式

 * 开发人员: renhui <br>
 * 开发时间: 2013-12-6 <br>
 * <br>
 */
public abstract class AbstractLengthCompareMode extends AbstractNoNeedValueCompareMode {
	
	public static final int DEFAULT_LEVEL_LENGTH = 2;
	
	private Integer lengthLevel;
	

	public Integer getLengthLevel() {
		return lengthLevel;
	}

	public void setLengthLevel(Integer lengthLevel) {
		this.lengthLevel = lengthLevel;
	}


	public BSONObject generateBSONObject(String propertyName, Object value, Context context) {
		Integer lengthLevel = null;
		if (context != null) {
			String lengthLevelName = ModelUtil.getSpliceParamterName(propertyName,
					MongoDbContext.LEVEL_LENGTH);
			Object objectValue=context.get(lengthLevelName);
			if(objectValue!=null){
				lengthLevel = Integer.parseInt(String.valueOf(objectValue));
			}
		}
		lengthLevel = ObjectUtil.defaultIfNull(lengthLevel,
				DEFAULT_LEVEL_LENGTH);
		int lengthValue=0;
		if (value != null) {
			lengthValue = String.valueOf(value).length();
			lengthValue += lengthLevel;
		}else{
			lengthValue=lengthLevel;
		}
		return lengthBSONObject(propertyName, lengthValue);
	}

	protected abstract BSONObject lengthBSONObject(String propertyName, int lengthValue) ;


}
