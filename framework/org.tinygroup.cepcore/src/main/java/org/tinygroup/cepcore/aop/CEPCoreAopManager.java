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
package org.tinygroup.cepcore.aop;

import org.tinygroup.event.Event;
import org.tinygroup.xmlparser.node.XmlNode;

public interface CEPCoreAopManager {
	String CEPCORE_AOP_BEAN = "cepcore_aop_bean";
	String BEFORE = "before";
	String AFTER = "after";
	String BEFORE_LOCAL = "before-local";
	String AFTER_LOCAL = "after-local";
	String BEFORE_REMOTE = "before-remote";
	String AFTER_REMOTE = "after-remote";

	void init(XmlNode config);

	void beforeLocalHandle(Event event);

	void afterLocalHandle(Event event);

	void beforeRemoteHandle(Event event);

	void afterRemoteHandle(Event event);

	void beforeHandle(Event event);

	void afterHandle(Event event);
	
	void addAopAdapter(String position,String beanName,String servicePatternString);
}
