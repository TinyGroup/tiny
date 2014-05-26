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
package org.tinygroup.datasource;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 动态数据源
 * 
 * @author chenjiao
 * 
 */
public class DynamicDataSource implements DataSource, ApplicationContextAware {

	public static final String DATASOURCE_NAME="dynamicDataSource";
	private static final Logger log = LoggerFactory
			.getLogger(DynamicDataSource.class);
	private ApplicationContext applicationContext = null;
	private DataSource dataSource = null;

	private Method unwrapMethod = null;
	private Method isWrapperForMethod = null;

	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return getDataSource().getConnection(username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return getDataSource().getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return getDataSource().getLoginTimeout();
	}

	public void setLogWriter(PrintWriter printWriter) throws SQLException {
		getDataSource().setLogWriter(printWriter);
	}

	public void setLoginTimeout(int timeout) throws SQLException {
		getDataSource().setLoginTimeout(timeout);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public DataSource getDataSource(String dataSourceName) {
		log.logMessage(LogLevel.DEBUG, "数据源名:" + dataSourceName);
		try {
			if (dataSourceName == null || dataSourceName.equals("")) {
				return this.dataSource;
			}
			return (DataSource) this.applicationContext.getBean(dataSourceName);
		} catch (NoSuchBeanDefinitionException ex) {
			throw new RuntimeException("There is not the dataSource <name:"
					+ dataSourceName + "> in the applicationContext!");
		}
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		String sp = DataSourceInfo.getDataSource();
		return getDataSource(sp);
	}

	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		DataSource dataSource = getDataSource();
		if (unwrapMethod == null) {
			unwrapMethod = getMethodByMethodName("unwrap");
		}
		try {
			return (T) unwrapMethod.invoke(dataSource, iface);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Method getMethodByMethodName(String methodName) {
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		throw new RuntimeException(
				"in org.tinygroup.datasource.DynamicDataSource : java.sql.DataSource has no method named \""
						+ methodName + "\"");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		DataSource dataSource = getDataSource();
		try {
			if (isWrapperForMethod == null) {
				isWrapperForMethod = getMethodByMethodName("isWrapperFor");
			}
			return (Boolean) isWrapperForMethod.invoke(dataSource, iface);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
