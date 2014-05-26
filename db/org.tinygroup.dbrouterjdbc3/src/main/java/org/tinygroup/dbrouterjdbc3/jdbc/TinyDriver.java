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
package org.tinygroup.dbrouterjdbc3.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 功能说明: tiny版本的驱动器
 * <p/>

 * 开发人员: renhui <br>
 * 开发时间: 2013-12-11 <br>
 * <br>
 */
public class TinyDriver implements Driver {

    private RouterManager manager;

    private Logger logger = LoggerFactory.getLogger(TinyDriver.class);

    private static I18nMessage i18nMessage = I18nMessageFactory
            .getI18nMessages();// 需要在启动的时候注入进来

    static {
        try {
            DriverManager.registerDriver(new TinyDriver());
        } catch (SQLException E) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    public TinyDriver() {
        manager = RouterManagerBeanFactory.getManager();
    }

    public RouterManager getManager() {
        return manager;
    }

    public void setManager(RouterManager manager) {
        this.manager = manager;
    }

    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }
        String routerName = url.substring("jdbc:dbrouter://".length());
        Router router = manager.getRouter(routerName);
        String user = info.getProperty("user");
        String password = info.getProperty("password");
        if (!user.equals(router.getUserName())) {
            logger.logMessage(LogLevel.ERROR,
                    "username {0} and {1} not equals", user,
                    router.getUserName());
            throw new SQLException("username not equals");
        }
        if (!password.equals(router.getPassword())) {
            logger.logMessage(LogLevel.ERROR,
                    "password {0} and {1} not equals", password,
                    router.getPassword());
            throw new SQLException("password not equals");
        }
        return new TinyConnection(routerName);
    }

    /**
     * 根据url可以找到集群配置则认为合法
     */
    public boolean acceptsURL(String url) throws SQLException {
        if (url.startsWith("jdbc:dbrouter://"))
            return true;
        return false;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
            throws SQLException {
        if (info == null) {
            info = new Properties();
        }
        DriverPropertyInfo userProp = new DriverPropertyInfo("user",
                info.getProperty("user"));
        userProp.required = true;
        userProp.description = i18nMessage
                .getMessage("dbrouter.driverproperty.user");

        DriverPropertyInfo passwordProp = new DriverPropertyInfo("password",
                info.getProperty("password"));

        passwordProp.required = true;
        passwordProp.description = i18nMessage
                .getMessage("dbrouter.driverproperty.password");

        DriverPropertyInfo[] dpi = new DriverPropertyInfo[2];
        dpi[0] = userProp;
        dpi[1] = passwordProp;
        return dpi;
    }

    public int getMajorVersion() {
        return 0;
    }

    public int getMinorVersion() {
        return 0;
    }

    public boolean jdbcCompliant() {
        return false;
    }

}
