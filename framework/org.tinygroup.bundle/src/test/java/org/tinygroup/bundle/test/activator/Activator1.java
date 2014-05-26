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
package org.tinygroup.bundle.test.activator;

import org.tinygroup.bundle.BundleActivator;
import org.tinygroup.bundle.BundleContext;
import org.tinygroup.bundle.BundleException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class Activator1 implements BundleActivator {
	
	private Logger logger = LoggerFactory.getLogger(Activator1.class);
	
	public void start(BundleContext bundleContext) throws BundleException {
		logger.logMessage(LogLevel.INFO, "================start================");
	}

	public void stop(BundleContext bundleContext) throws BundleException {
		logger.logMessage(LogLevel.INFO, "================stop================");
	}

}
