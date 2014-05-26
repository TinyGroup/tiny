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
package org.tinygroup.uiengine;

import junit.framework.TestCase;

import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;

public class TestFileProcessor extends TestCase {
	private static UIComponentManager manager;
	static {
		TestInit.init();
		manager = SpringUtil.getBean(
				UIComponentManager.UIComponentManager_BEAN);
	}

	public void testGet() {
		UIComponent component = manager.getUiComponents().iterator().next();
		assertEquals("x.css".equals(component.getCssResource()), true);
	}
}
