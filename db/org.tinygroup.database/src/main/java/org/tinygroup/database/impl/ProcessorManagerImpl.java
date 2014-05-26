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
package org.tinygroup.database.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.processor.Processor;
import org.tinygroup.database.config.processor.Processors;
import org.tinygroup.springutil.SpringUtil;

public class ProcessorManagerImpl implements ProcessorManager {
	Map<String,Map<String,Object>> processorsMap  = new HashMap<String,Map<String,Object>>();

	public void addPocessors(Processors processors) {
		String language = processors.getLanguage();
		
		if(!processorsMap.containsKey(language)){
			processorsMap.put(language, new HashMap<String,Object>());
		}
		Map<String,Object> map = processorsMap.get(language);
		for(Processor processor: processors.getList()){
			String processorName = processor.getName();
			String bean = processor.getBean();
			map.put(processorName,SpringUtil.getBean(bean));
		}
	}
	
	public void removePocessors(Processors processors) {
		String language = processors.getLanguage();
		Map<String,Object> map = processorsMap.get(language);
		if(!CollectionUtil.isEmpty(map)){
			for(Processor processor: processors.getList()){
				String processorName = processor.getName();
				map.remove(processorName);
			}
		}
		
	}

	public Object getProcessor(String language, String name) {
		if(processorsMap.containsKey(language)){
			return processorsMap.get(language).get(name);
		}
		return null;
	}

}
