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
package org.tinygroup.imda.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 模型处理器定义
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("model-processor-define")
public class ModelProcessorDefine {
	@XStreamAsAttribute
	@XStreamAlias("record-mode")
	String recordMode;// 操作记录的方式，可以是None(无),Single(单记录),Multiple(多记录)

	@XStreamAsAttribute
	private String name;// 模型类型名称

	@XStreamAsAttribute
	private String title;// 模型类型标识
	@XStreamAlias("page-config")
	PageConfig pageConfig;

	public String getRecordMode() {
		if (recordMode == null || recordMode.length() == 0) {
			return "none";
		}
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	@XStreamImplicit
	List<ModelProcessorStage> processorStages;

	public List<ModelProcessorStage> getProcessorStages() {
		if (processorStages == null)
			processorStages = new ArrayList<ModelProcessorStage>();
		return processorStages;
	}

	public void setProcessorStages(List<ModelProcessorStage> processorStages) {
		this.processorStages = processorStages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PageConfig getPageConfig() {
		return pageConfig;
	}

	public void setPageConfig(PageConfig pageConfig) {
		this.pageConfig = pageConfig;
	}

	
	

}
