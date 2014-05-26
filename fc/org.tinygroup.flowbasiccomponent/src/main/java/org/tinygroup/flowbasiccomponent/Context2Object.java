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
package org.tinygroup.flowbasiccomponent;

import org.tinygroup.context.Context;
import org.tinygroup.context2object.fileresolver.GeneratorFileProcessor;
import org.tinygroup.context2object.impl.ClassNameObjectGenerator;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

public class Context2Object implements ComponentInterface {
	private static Logger logger = LoggerFactory.getLogger(Context2Object.class);
	private String className;
	private String collectionClassName;
	private String resultKey;
	private String varName;
	private String beanName;
	private static String DEFAULT_KEY = "context2Object_result";

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getCollectionClassName() {
		return collectionClassName;
	}

	public void setCollectionClassName(String collectionClassName) {
		this.collectionClassName = collectionClassName;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}

	public void execute(Context context) {
		ClassNameObjectGenerator generator = SpringUtil.getBean(GeneratorFileProcessor.CLASSNAME_OBJECT_GENERATOR_BEAN);
		if (isNull(resultKey))
			resultKey = varName;
		if(isNull(resultKey)){
			//20130808注释LoaderManagerFactory
//			resultKey = LoaderManagerFactory.getManager().getClass(className).getSimpleName().toLowerCase();
			try {
				resultKey = Class.forName(className).getSimpleName().toLowerCase();
			} catch (ClassNotFoundException e) {
				logger.logMessage(LogLevel.WARN, e.getMessage());
			}
			if(isNull(resultKey))
				resultKey = DEFAULT_KEY;
		}
			
		
		if (collectionClassName == null || "".equals(collectionClassName)) {
			context.put(resultKey,
					generator.getObject(varName,beanName, className, context));
		} else {
			context.put(resultKey, generator.getObjectCollection(varName,
					collectionClassName, className, context));
		}

	}
	
	private boolean isNull(String str){
		if(str==null||"".equals(str.trim()))
			return true;
		return false;
	}

}
