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
package org.tinygroup.event;

import org.tinygroup.context.Context;

import java.io.Serializable;

/**
 * 服务请求
 *
 * @author luoguo
 */
public final class ServiceRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3221824083023375172L;
    /**
     * 节点名称，如果有指定节点名称，则只能访问指定节点上的服务
     */
    
    private String nodeName;
    /**
     * 组织标识
     */
    private String serviceId;
    /**
     * 服务请求所带的参数
     */
    private Context context;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}