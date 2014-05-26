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
package org.tinygroup.entity.impl;

import java.util.List;

import org.tinygroup.commons.tools.Assert;


/**
 * 
 * 功能说明:not between and 比较符 

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-16 <br>
 * <br>
 */
public class NotBetweenAndCompareMode extends AbstractCompareMode {

	public String generateCompareSymbols(String fieldName) {
		return fieldName+" not between ? and ? ";
	}

	public String getCompareKey() {
		return "notBetweenAnd";
	}

	
	protected void formatValueArray(Object[] array, List<Object> params) {
	    Assert.assertTrue(array.length==2,"betweenAnd比较符的值长度为2");
		super.formatValueArray(array, params);
	}

	
	

}
