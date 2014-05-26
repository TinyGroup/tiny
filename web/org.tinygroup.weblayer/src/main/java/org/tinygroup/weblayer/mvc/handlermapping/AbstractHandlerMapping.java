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
package org.tinygroup.weblayer.mvc.handlermapping;

import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.mvc.HandlerMapping;
import org.tinygroup.weblayer.mvc.MappingModelManager;

/**
 * 
 * 功能说明: 抽象的handlermapping

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-22 <br>
 * <br>
 */
public abstract class AbstractHandlerMapping implements HandlerMapping {

	private MappingModelManager manager;
	
	
	public MappingModelManager getManager() {
		return manager;
	}

	public void setManager(MappingModelManager manager) {
		this.manager = manager;
	}
	

	public void init() {
		if(manager==null){
			manager=SpringUtil.getBean("mappingModelManager");
		}
	}

}
