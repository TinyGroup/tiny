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
package org.tinygroup.database.view.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.view.View;
import org.tinygroup.database.config.view.Views;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.database.view.ViewProcessor;
import org.tinygroup.database.view.ViewSqlProcessor;
import org.tinygroup.springutil.SpringUtil;

public class ViewProcessorImpl implements ViewProcessor {
	private static Map<String, View> viewMap = new HashMap<String, View>();

	public void addViews(Views views) {
		for (View view : views.getViewTableList()) {
			viewMap.put(view.getName(), view);
		}

	}
	
	public void removeViews(Views views) {
		for (View view : views.getViewTableList()) {
			viewMap.remove(view.getName());
		}

	}

	public View getView(String name) {
		if (!viewMap.containsKey(name)) {
			throw new RuntimeException(String.format("视图[name:%s]不存在,", name));
		}
		return viewMap.get(name);
	}

	public String getCreateSql(String name, String language) {
		View view = getView(name);
		return getCreateSql(view, language);
	}

	public String getCreateSql(View view, String language) {
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		ViewSqlProcessor sqlProcessor = (ViewSqlProcessor) processorManager
				.getProcessor(language, "view");

		return sqlProcessor.getCreateSql(view);
	}

	public List<String> getCreateSql(String language) {
		List<String> list = new ArrayList<String>();
		for (View view : viewMap.values()) {
			list.add(getCreateSql(view, language));
		}

		return list;
	}

	public String getDropSql(String name, String language) {
		View view = getView(name);
		return getDropSql(view, language);
	}

	public String getDropSql(View view, String language) {
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		ViewSqlProcessor sqlProcessor = (ViewSqlProcessor) processorManager
				.getProcessor(language, "view");
		return sqlProcessor.getDropSql(view);
	}

	public List<String> getDropSql(String language) {
		List<String> list = new ArrayList<String>();
		for (View view : viewMap.values()) {
			list.add(getDropSql(view, language));
		}

		return list;
	}

	public List<View> getViews() {
		List<View> list = new ArrayList<View>();
		list.addAll(viewMap.values());
		return list;
	}

}
