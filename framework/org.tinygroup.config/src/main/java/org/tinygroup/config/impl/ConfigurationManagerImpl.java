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
package org.tinygroup.config.impl;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能说明: 应用程序配置管理接口的默认实现，完成管理各个应用处理器的配置信息。配置详细信息定义在application.xml文件中。
 * 根节点为application
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-2-22 <br>
 * <br>
 */
public class ConfigurationManagerImpl implements ConfigurationManager {
    private static Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);
    private Map<String, XmlNode> componentConfigMap = new HashMap<String, XmlNode>();
    private XmlNode applicationConfig = null;
    // 保存各个应用处理器配置信息列表
    private Collection<Configuration> configurationList;
    public Map<String, String> applicationPropertiesMap = new HashMap<String, String>();
    private Map<String, Long> fileDateMap = new HashMap<String, Long>();

    public void setConfigurationList(Collection<Configuration> configurationList) {
        this.configurationList = configurationList;
    }

    public Map<String, XmlNode> getComponentConfigMap() {
        return componentConfigMap;
    }

    private static String replace(String content, String name, String value) {

        Pattern pattern = Pattern.compile("[{]" + name + "[}]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer buf = new StringBuffer();
        int curpos = 0;
        while (matcher.find()) {
            buf.append(content.substring(curpos, matcher.start()));
            curpos = matcher.end();
            buf.append(value);
            continue;
        }
        buf.append(content.substring(curpos));
        return buf.toString();
    }

    public ConfigurationManagerImpl() {
    }

    public void loadApplicationConfig(String config) {
        applicationConfig = new XmlStringParser().parse(config).getRoot();// 第一次解出
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationConfig);
        List<XmlNode> propertyList = filter.findNodeList("/application/application-properties/property");
        for (XmlNode property : propertyList) {
            String name = property.getAttribute("name");
            String value = property.getAttribute("value");
            applicationPropertiesMap.put(name, value);
        }

        config = replaceProperty(config);// 替换里面的全局变量
        applicationConfig = new XmlStringParser().parse(config).getRoot();// 再次解析，出来最终结果

    }

    private String replaceProperty(String config) {
        if (applicationPropertiesMap.size() > 0) {
            for (String name : applicationPropertiesMap.keySet()) {
                String value = applicationPropertiesMap.get(name);
                config = replace(config, name, value);
            }
        }
        return config;
    }

    public void loadComponentConfig(FileObject fileObject) {
        try {
            String config = StreamUtil.readText(fileObject.getInputStream(), "UTF-8", true);
            String path = fileObject.getPath();
            if (applicationConfig != null) {
                config = replaceProperty(config);
            }
            Long lastModifiedTime = fileDateMap.get(path);
            long modifiedTime = fileObject.getLastModifiedTime();
            if (lastModifiedTime == null || lastModifiedTime != modifiedTime) {
                XmlNode xmlNode = new XmlStringParser().parse(config).getRoot();
                componentConfigMap.put(path, xmlNode);
                fileDateMap.put(path, modifiedTime);
            }
        } catch (IOException e) {
            logger.errorMessage("读取组件配置文件:{0}时发生错误：{}", e, fileObject.getFileName(), e.getMessage());
        }

    }

    public void unloadComponentConfig(String path) {
        componentConfigMap.remove(path);
        fileDateMap.remove(path);
    }

    public void distributeConfig() {
        if (configurationList != null) {
            logger.logMessage(LogLevel.INFO, "正在分发应用配置信息...");
            PathFilter<XmlNode> pathFilter = new PathFilter<XmlNode>(applicationConfig);
            for (Configuration configuration : configurationList) {
                XmlNode componentConfig = componentConfigMap.get(configuration.getComponentConfigPath());
                XmlNode appConfig = null;
                if (configuration.getApplicationNodePath() != null) {
                    appConfig = pathFilter.findNode(configuration.getApplicationNodePath());
                }
                configuration.config(appConfig, componentConfig);
            }
            logger.logMessage(LogLevel.INFO, "应用配置信息分发完毕");
        }
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    public Collection<Configuration> getConfigurationList() {
        return configurationList;
    }

    public Map<String, String> getApplicationPropertiesMap() {
        return applicationPropertiesMap;
    }

    public void setApplicationProperty(String key, String value) {
        applicationPropertiesMap.put(key, value);
    }

    public String getApplicationProperty(String key, String defaultValue) {
        String value = applicationPropertiesMap.get(key);
        if (value == null || value.length() == 0) {
            value = defaultValue;
        }
        return value;
    }

    public String getApplicationProperty(String key) {
        return applicationPropertiesMap.get(key);
    }

}
