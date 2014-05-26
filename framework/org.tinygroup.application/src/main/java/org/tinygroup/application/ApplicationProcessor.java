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
package org.tinygroup.application;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.config.Configuration;


public interface ApplicationProcessor extends Configuration,Ordered{
	/**
	 * 
	 * 应用程序处理器开启方法
	 */
	void start();

	/**
	 * 
	 * 应用程序处理器关闭方法
	 */
	void stop();
	
    /**
     * 	
     *设置本应用处理器所属的应用程序
     * @param application
     */
	void setApplication(Application application);

}
