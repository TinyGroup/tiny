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

/**
 * 用于定义模型
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("model-define")
public class ModelDefine {
	@XStreamAsAttribute
	private String id;// 模型类型标识

	@XStreamAsAttribute
	private String name;// 模型类型英文名称

	@XStreamAsAttribute
	private String title;// 模型类型中文名称
	@XStreamAlias("model-permission-checker-bean")
	@XStreamAsAttribute
	String modelPermissionCheckerBean;// 权限检查器的Bean
	@XStreamAlias("error-page")
	@XStreamAsAttribute
	String errorPage;// 出现错误时转向的页面
	@XStreamAlias("validate-error-page")
	@XStreamAsAttribute
	String validateErrorPage;// 校验失败时转身的页面

	public String getValidateErrorPage() {
		return validateErrorPage;
	}

	public void setValidateErrorPage(String validateErrorPage) {
		this.validateErrorPage = validateErrorPage;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	public String getModelPermissionCheckerBean() {
		return modelPermissionCheckerBean;
	}

	public void setModelPermissionCheckerBean(String modelPermissionCheckerBean) {
		this.modelPermissionCheckerBean = modelPermissionCheckerBean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XStreamAsAttribute
	@XStreamAlias("model-class")
	private String modelClass;// 模型的类名

	@XStreamAsAttribute
	@XStreamAlias("model-loader-bean")
	private String modelLoaderBean;// 模型载入的Bean

	@XStreamAsAttribute
	@XStreamAlias("model-infomation-getter")
	private String modelInfomationGetterBean;// 模型信息获取bean

	@XStreamAlias("model-processor-defines")
	private List<ModelProcessorDefine> modelProcessorDefines;// 模型处理器定义

	public String getModelLoaderBean() {
		return modelLoaderBean;
	}

	public void setModelLoaderBean(String modelLoaderBean) {
		this.modelLoaderBean = modelLoaderBean;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
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

	public String getModelInfomationGetterBean() {
		return modelInfomationGetterBean;
	}

	public void setModelInfomationGetterBean(String modelInfomationGetterBean) {
		this.modelInfomationGetterBean = modelInfomationGetterBean;
	}

	public List<ModelProcessorDefine> getModelProcessorDefines() {
		if (modelProcessorDefines == null)
			modelProcessorDefines = new ArrayList<ModelProcessorDefine>();
		return modelProcessorDefines;
	}

	public void setModelProcessorDefines(
			List<ModelProcessorDefine> modelProcessorDefines) {
		this.modelProcessorDefines = modelProcessorDefines;
	}

}
