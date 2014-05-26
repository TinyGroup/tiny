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
package org.tinygroup.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class MongodbConfig {

	private static Logger logger = LoggerFactory.getLogger(MongodbConfig.class);

	public static final String HOST;
	public static final int PORT;
	public static final String DB_NAME;
	public static final String USER_NAME;
	public static final String PASSWORD;

	static {
		Properties prop = new Properties();
		InputStream in = MongodbConfig.class
				.getResourceAsStream("/mongodb.config");
		try {
			prop.load(in);
		} catch (IOException e) {
			logger.errorMessage("加载配置文件失败/mongodb.config", e);
			throw new RuntimeException("加载配置文件失败/mongodb.config");
		}

		HOST = prop.getProperty("host");
		PORT = Integer.valueOf(prop.getProperty("port"));
		DB_NAME = prop.getProperty("dbname");
		USER_NAME = prop.getProperty("username");
		PASSWORD = prop.getProperty("password");
	}

}
