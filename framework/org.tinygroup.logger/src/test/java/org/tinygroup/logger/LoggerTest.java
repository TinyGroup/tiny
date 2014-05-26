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
package org.tinygroup.logger;

import static org.tinygroup.logger.LogLevel.DEBUG;
import static org.tinygroup.logger.LogLevel.ERROR;
import static org.tinygroup.logger.LogLevel.INFO;
import static org.tinygroup.logger.LogLevel.TRACE;
import static org.tinygroup.logger.LogLevel.WARN;

import java.util.Locale;
import java.util.Properties;

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.i18n.I18nMessageFactory;

public class LoggerTest extends TestCase {

	static Logger logger = LoggerFactory.getLogger(LoggerTest.class);

	protected void setUp() throws Exception {
		super.setUp();
		Properties properties = new Properties();
		properties.load(this.getClass().getResourceAsStream(
				"/i18n/info_zh_CN.properties"));
		I18nMessageFactory.addResource(Locale.SIMPLIFIED_CHINESE, properties);
	}

	public void testStartBusinessLog() {
		System.out.println("------------1----------");
		logger.startTransaction();
		System.out.println("------------2----------");
		logger.logMessage(INFO, "start");
		System.out.println("------------3----------");
		new HelloA().sayHello();
		System.out.println("------------4----------");
		new HelloB().sayHello();
		System.out.println("------------5----------");
		logger.logMessage(INFO, "end");
		System.out.println("------------6----------");
		logger.endTransaction();
		System.out.println("------------7----------");
	}

	public void testStartBusinessLogANotSupport() {
		System.out.println("------------1----------");
		logger.startTransaction();
		System.out.println("------------2----------");
		logger.logMessage(INFO, "start");
		System.out.println("------------3----------");
		HelloA helloA = new HelloA();
		HelloA.logger.setSupportTransaction(false);
		helloA.sayHello();
		System.out.println("------------4----------");
		new HelloB().sayHello();
		System.out.println("------------5----------");
		logger.logMessage(INFO, "end");
		System.out.println("------------6----------");
		logger.endTransaction();
		System.out.println("------------7----------");
	}

	public void testLog() {
		logger.log(DEBUG, "name");
		logger.log(ERROR, "name");
		logger.log(INFO, "name");
		logger.log(WARN, "name");
		logger.log(TRACE, "name");
	}

	public void testLogMessage() {
		if (logger.isEnabled(ERROR)) {
			logger.logMessage(ERROR, "n{0}a{1}m{2}e", "-", "&", "|");
		}
	}

	public void testLogMessageContext() {
		Context c = new ContextImpl();
		c.put("a", 1);
		c.put("b", 2);
		c.put("c", 3);
		logger.logMessage(ERROR, "n${a}a${b}m${c}e", c);
	}

	public void testIsEnabled() {
		assertEquals(false, logger.isEnabled(DEBUG));
		assertEquals(false, logger.isEnabled(TRACE));
		assertEquals(true, logger.isEnabled(ERROR));
		assertEquals(true, logger.isEnabled(INFO));
		assertEquals(true, logger.isEnabled(WARN));
	}

}
