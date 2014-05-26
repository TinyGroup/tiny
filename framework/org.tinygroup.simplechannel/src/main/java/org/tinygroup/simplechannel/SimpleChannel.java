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
package org.tinygroup.simplechannel;

import java.util.List;

import org.tinygroup.channel.impl.AbstractChannel;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

public class SimpleChannel extends AbstractChannel {

	public String getId() {
		return this.getClass().getSimpleName();
	}

	protected void receive(Event event) {
		sendEvent(event);

	}

	public List<ServiceInfo> getServiceInfos() {
		return null;
	}

	public int getType() {
		return TYPE_CHANNEL;
	}

}
