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
package org.tinygroup.uiengine.manager.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.config.UIComponents;
import org.tinygroup.uiengine.manager.UIComponentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIComponentManagerImpl implements UIComponentManager {
    static Logger logger = LoggerFactory.getLogger(UIComponentManagerImpl.class);
    private List<UIComponent> uiComponentList = new ArrayList<UIComponent>();
    List<UIComponent> healthyComponentList = new ArrayList<UIComponent>();
    private Map<String, UIComponent> uiMap = new HashMap<String, UIComponent>();

    public List<UIComponent> getUiComponents() {
        return uiComponentList;
    }

    public List<UIComponent> getHealthUiComponents() {
        for (UIComponent component : uiComponentList) {
            this.isHealth(component.getName());
        }
        return healthyComponentList;
    }

    public void addUIComponents(UIComponents uiComponents) {
        uiComponentList.addAll(uiComponents.getComponents());
        for (UIComponent component : uiComponents.getComponents()) {
            uiMap.put(component.getName(), component);
        }
    }

    public boolean isHealth(String name) {
        UIComponent component = getUIComponent(name);
        if (component == null) {
            throw new RuntimeException("找不到UI组件包：" + name);
        }
        if (component.isComputed()) {// 如果已经计算好，则直接返回
            return component.isHealth();
        } else {
            // 如果不依赖，则健康
            if (component.getDependencies() == null || component.getDependencies().trim().length() == 0) {
                component.setComputed(true);
                component.setHealth(true);
                healthyComponentList.add(component);
                return true;
            } else {
                String[] dependencies = component.getDependencies().split(",");
                for (String dependencyName : dependencies) {
                    if (!isHealth(dependencyName)) {
                        logger.logMessage(LogLevel.ERROR, "UI包<{}>依赖的<{}>UI包，不能被找到，因此不会被加载！依赖此包的应用可能不会被顺序执行。", name, dependencyName);
                        component.setComputed(true);
                        component.setHealth(false);
                        return false;
                    }
                }
                component.setComputed(true);
                component.setHealth(true);
                healthyComponentList.add(component);
                return true;
            }
        }
    }

    public UIComponent getUIComponent(String name) {
        return uiMap.get(name);
    }

    public String[] getComponentJsArray(UIComponent component, boolean isDebug) {
        String path = component.getJsResource();
        if (path != null && path.trim().length() > 0) {
            String[] paths = path.trim().split(",");
            for (int i = 0; i < paths.length; i++) {
                paths[i] = paths[i].trim();
            }
            return paths;
        }
        return null;
    }

    public String[] getComponentCssArray(UIComponent component, boolean isDebug) {
        String path = component.getCssResource();
        if (path != null && path.trim().length() > 0) {
            String[] paths = path.trim().split(",");
            for (int i = 0; i < paths.length; i++) {
                paths[i] = paths[i].trim();
            }
            return paths;
        }
        return null;
    }

    public void removeUIComponents(UIComponents uiComponents) {
        uiComponentList.removeAll(uiComponents.getComponents());
        for (UIComponent component : uiComponents.getComponents()) {
            uiMap.remove(component.getName());
            healthyComponentList.remove(component);
        }
    }
}
