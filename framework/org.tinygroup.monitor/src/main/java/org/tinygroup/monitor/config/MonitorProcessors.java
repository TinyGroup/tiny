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
package org.tinygroup.monitor.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("monitor-processors")
public class MonitorProcessors {
	@XStreamAsAttribute
	@XStreamAlias("group-id")
	private String groupId;
	@XStreamAsAttribute
	@XStreamAlias("artifact-id")
	private String artifactId;
	@XStreamImplicit
	private List<MonitorProcessor> monitorProcessors;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}


	public List<MonitorProcessor> getMonitorProcessors() {
		return monitorProcessors;
	}

	public void setMonitorProcessors(List<MonitorProcessor> monitorProcessors) {
		this.monitorProcessors = monitorProcessors;
	}

}
