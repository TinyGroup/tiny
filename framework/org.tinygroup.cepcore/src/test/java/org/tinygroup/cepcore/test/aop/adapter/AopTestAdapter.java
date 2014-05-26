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
package org.tinygroup.cepcore.test.aop.adapter;

import org.tinygroup.cepcore.aop.CEPCoreAopAdapter;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class AopTestAdapter implements CEPCoreAopAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(AopTestAdapter.class);
	public void handle(Event event) {
		logger.logMessage(LogLevel.INFO, "====================================");
		logger.logMessage(LogLevel.INFO, "aopTest:"+event.getServiceRequest().getServiceId());
		logger.logMessage(LogLevel.INFO, "====================================");
	}

}
