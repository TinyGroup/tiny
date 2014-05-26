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
package org.tinygroup.uiengine.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 定义UI组件
 *
 * @author luoguo
 */
@XStreamAlias("ui-component")
public class UIComponent {
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String title;
    @XStreamAsAttribute
    private String category;
    @XStreamAsAttribute
    private String icon;
    private String description;
    @XStreamAsAttribute
    private String help;
    @XStreamAsAttribute
    private String dependencies;// 依赖哪些，如果依赖多个，用","分隔
    @XStreamAlias("css-resource")
    private String cssResource;
    @XStreamAlias("js-resource")
    private String jsResource;
    @XStreamAlias("css-codelet")
    private String cssCodelet;
    @XStreamAlias("js-codelet")
    private String jsCodelet;
    private Macros macros;
    private transient boolean health;
    private transient boolean computed = false;

    public Macros getMacros() {
        return macros;
    }

    public void setMacros(Macros macros) {
        this.macros = macros;
    }

    public boolean isComputed() {
        return computed;
    }

    public void setComputed(boolean computed) {
        this.computed = computed;
    }

    public boolean isHealth() {
        return health;
    }

    public void setHealth(boolean health) {
        this.health = health;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }


    public String getHelp() {
        return getInfo(help);
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getCategory() {
        return getInfo(category);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCssResource() {
        return getInfo(cssResource);
    }

    private String getInfo(String str) {
        if (str != null) {
            String string = str.trim();
            if (string.length() == 0) {
                string = null;
            }
            return string;
        }
        return null;
    }

    public void setCssResource(String cssResource) {
        this.cssResource = cssResource;
    }

    public String getJsResource() {
        return getInfo(jsResource);
    }

    public void setJsResource(String jsResource) {
        this.jsResource = jsResource;
    }

    public String getCssCodelet() {
        return getInfo(cssCodelet);
    }

    public void setCssCodelet(String cssCodelet) {
        this.cssCodelet = cssCodelet;
    }

    public String getJsCodelet() {
        return getInfo(jsCodelet);
    }

    public String getName() {
        return getInfo(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return getInfo(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return getInfo(icon);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return getInfo(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setJsCodelet(String jsCodelet) {
        this.jsCodelet = jsCodelet;
    }

}
