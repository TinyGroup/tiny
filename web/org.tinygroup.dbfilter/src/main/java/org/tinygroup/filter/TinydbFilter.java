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
package org.tinygroup.filter;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.util.TinyDBUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;

/**
 * 
 * 功能说明:获取beantype参数,组装成Bean对象,存放到context 
 * 开发人员: renhui <br>
 * 开发时间: 2014-2-20 <br>
 */
public class TinydbFilter extends AbstractTinyFilter{
	
	private static final String SPLIT = ",";
	private static final String BEAN_TYPE_KEY="@beantype";
	private Logger logger=LoggerFactory.getLogger(TinydbFilter.class);


	public void preProcess(WebContext context) {
		String beanType=context.get(BEAN_TYPE_KEY);
		if(!StringUtil.isBlank(beanType)){
			String[] types=beanType.split(SPLIT);
			for (String type : types) {
				if(context.get(type)!=null){
					logger.logMessage(LogLevel.WARN, "已经存在名称：{0}的参数",type);
				}else{
					Bean bean=TinyDBUtil.context2Bean(context, type);
					context.put(type, bean);
				}
			}
		}
	}
	
}
