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
package org.tinygroup.flowplugin;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.impl.AbstractEventProcessor;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FlowPluginProcessor extends AbstractEventProcessor {
    /**
     * 流程执行器，用于执行具体的Service
     */
    private List<FlowExecutor> executors = new ArrayList<FlowExecutor>();

    private static Logger logger = LoggerFactory.getLogger(FlowPluginProcessor.class);

    public void process(Event event) {
        Flow flow = null;
        FlowExecutor flowExecutor = null;
        String nodeId = null;
        String serviceId = event.getServiceRequest().getServiceId();
        String flowId = serviceId;
        String[] str = serviceId.split(":");
        if (str.length > 1) {
            nodeId = str[str.length - 1];
            flowId = serviceId.substring(0, serviceId.length() - nodeId.length() - 1);
        }
        for (FlowExecutor executor : executors) {
            flow = executor.getFlow(flowId);
            if (flow != null) {
                flowExecutor = executor;
                break;
            }
        }
        if (flow != null) {
            flowExecutor.execute(flowId, nodeId, (Context) event.getServiceRequest().getContext());
        } else {
            logger.logMessage(LogLevel.ERROR, "[Flow:{0}]不存在或无合适的Flow流程执行器", flowId);
            throw new RuntimeException("[Flow:" + flowId + "]不存在或无合适的Flow流程执行器");
        }

    }

    public void setCepCore(CEPCore cepCore) {
    }

    public void addExecutor(FlowExecutor executor) {
        this.executors.add(executor);
    }

    public List<ServiceInfo> getServiceInfos() {
        List<ServiceInfo> list = new ArrayList<ServiceInfo>();
        for (Flow f : executors.get(0).getFlowIdMap().values()) {
            list.add(new FlowServiceInfo(f));
        }
        return list;
    }

}
