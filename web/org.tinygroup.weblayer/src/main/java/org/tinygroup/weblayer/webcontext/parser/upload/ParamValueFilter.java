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
package org.tinygroup.weblayer.webcontext.parser.upload;

import org.tinygroup.weblayer.WebContext;

/**
 * 
 * 功能说明:参数值过滤 

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-14 <br>
 * <br>
 */
public interface ParamValueFilter extends ParameterParserFilter {
	
	/**
	 * 
	 *  配置需要参数值过滤的正则
	 * @param pattern
	 */
	void setPattern(String patternStr);
   /**
    * 
    * 参数过滤方法
    * @param 要过滤的参数值
    * @param webContext 操作上下文
    * @return
    */
	String valueFilter(String value,WebContext webContext);
	/**
	 * 
	 * 参数名称是否匹配
	 * @param keyName
	 * @return
	 */
	boolean isFilter(String keyName);
	
}
