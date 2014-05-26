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

import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.fileresolver.impl.I18nFileProcessor;
import org.tinygroup.fileresolver.impl.SpringBeansFileProcessor;
import org.tinygroup.fileresolver.impl.XStreamFileProcessor;
import org.tinygroup.uiengine.fileresolver.UIComponentFileProcessor;

public class TestInit {
	static boolean inited = false;

	public static void init() {
		if (!inited) {
			inited = true;
			FileResolverFactory.getFileResolver().addFileProcessor(
					new XStreamFileProcessor());

			FileResolverFactory.getFileResolver().addFileProcessor(
					new I18nFileProcessor());
			FileResolverFactory.getFileResolver().addFileProcessor(
					new SpringBeansFileProcessor());
			FileResolverFactory.getFileResolver().addFileProcessor(
					new UIComponentFileProcessor());
			FileResolverFactory.getFileResolver().resolve();
		}
	}
}
