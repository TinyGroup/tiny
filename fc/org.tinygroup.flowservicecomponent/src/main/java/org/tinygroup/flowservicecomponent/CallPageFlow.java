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
package org.tinygroup.flowservicecomponent;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.springutil.SpringUtil;

public class CallPageFlow implements ComponentInterface {
	String flowId;
	String version;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void execute(Context context) {
		FlowExecutor executor = SpringUtil.getBean(
				FlowExecutor.PAGE_FLOW_BEAN);
		if (notNull(version)) {
			executor.execute(flowId, null, version, context);
		} else {
			executor.execute(flowId, context);
		}

	}

	private boolean notNull(String str) {
		return str != null && !"".equals(str);
	}

}
