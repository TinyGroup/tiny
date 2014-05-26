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
package org.tinygroup.pageflowbasiccomponent;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.parser.valueparser.ParameterParser;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

public class ParserContext2Object implements ComponentInterface {

	private String className;
	private String beanName;
	private String resultKey;
	private static String DEFAULT_KEY = "parserContext2Object_result";

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}

	public void execute(Context context) {
		Assert.assertTrue(!(StringUtil.isEmpty(beanName)&&StringUtil.isEmpty(className)),"beanName and className can not all be null");
		Object object=null;
		Class<?> clazz=null;
		if(!StringUtil.isEmpty(className)){
			//20130808注释LoaderManagerFactory
//			clazz = LoaderManagerFactory.getManager().getClass(className);
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				
			}
		}
		
		if(StringUtil.isEmpty(beanName)){
			if(clazz!=null){
				object=SpringUtil.getBean(clazz);
			}
		}else{
			if(clazz==null){
				object=SpringUtil.getBean(beanName);
			}else{
				object=SpringUtil.getBean(beanName, clazz);
			}
		}
		ParserWebContext parser = WebContextUtil.findWebContext(
				(WebContext) context, ParserWebContext.class);
		if (parser != null) {
			ParameterParser parameterParser = parser.getParameters();

			if (StringUtil.isEmpty(resultKey)) {
				resultKey = clazz.getSimpleName().toLowerCase();
				if (StringUtil.isEmpty(resultKey))
					resultKey = DEFAULT_KEY;
			}
			parameterParser.setProperties(object);
			context.put(resultKey,object);
		}else{
			throw new RuntimeException("can not convert object with pasercontext2object");
		}

	}

}
