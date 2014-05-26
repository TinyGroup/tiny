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
import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.WebContext;


/**
 * 
 * 功能说明:  对象session操作组件

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-21 <br>
 * <br>
 */
public class SessionOperate implements ComponentInterface {
	
	private static final Logger logger=LoggerFactory.getLogger(SessionOperate.class);

	private String sessionKey;
	
	private String contextKey;
	
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getContextKey() {
		return contextKey;
	}

	public void setContextKey(String contextKey) {
		this.contextKey = contextKey;
	}

	public void execute(Context context) {
		Assert.assertNotNull(contextKey, "contextKey must not null");
		Assert.assertNotNull(sessionKey, "sessionKey must not null");
		Object object=context.get(contextKey);
		if(!ObjectUtil.isEmptyObject(object)){
			WebContext webContext=(WebContext)context;
			logger.logMessage(LogLevel.INFO, "put object:[{}] to session,the key is [{}]",object,sessionKey);
			webContext.getRequest().getSession(true).setAttribute(sessionKey, object);
		}else{
			logger.logMessage(LogLevel.WARN, "not found object with key:[{}] from context",contextKey);
		}
		
		
	}

}
