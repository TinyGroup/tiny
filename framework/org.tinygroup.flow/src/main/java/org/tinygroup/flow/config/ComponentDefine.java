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
package org.tinygroup.flow.config;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.event.Parameter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 组件定义节点
 * 
 * @author renhui
 * 
 */
@XStreamAlias("component")
public class ComponentDefine {
	@XStreamAsAttribute
	private String category;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String bean;
	@XStreamAsAttribute
	private String title;
	@XStreamAsAttribute
	private String icon;
	@XStreamAsAttribute
	@XStreamAlias("short-description")
	private String shortDescription;
	@XStreamAsAttribute
	@XStreamAlias("long-description")
	private String longDescription;

	@XStreamImplicit
	private List<Parameter> parameters;
	@XStreamImplicit
	private List<Result> results;
	
	public ComponentDefine(){
		parameters=new ArrayList<Parameter>();
		results=new ArrayList<Result>();
	}

	
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public void addParamter(Parameter parameter){
		parameters.add(parameter);
	}
	
	public void removeParamter(Parameter parameter){
		parameters.remove(parameter);
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public void addResult(Result result){
		results.add(result);
	}
	
	public void removeResult(Result result){
		results.remove(result);
	}
	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

}
