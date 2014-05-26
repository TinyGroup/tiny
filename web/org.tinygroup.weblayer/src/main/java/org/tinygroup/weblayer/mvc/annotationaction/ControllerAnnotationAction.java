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
package org.tinygroup.weblayer.mvc.annotationaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.tinygroup.annotation.AnnotationClassAction;
import org.tinygroup.annotation.AnnotationMethodAction;
import org.tinygroup.commons.tools.AnnotationUtils;
import org.tinygroup.weblayer.mvc.MappingClassModel;
import org.tinygroup.weblayer.mvc.MappingMethodModel;
import org.tinygroup.weblayer.mvc.MappingModelManager;
import org.tinygroup.weblayer.mvc.annotation.RequestMapping;

/**
 * 
 * 功能说明:在类注解上发现控制层@Controller,并在方法上发现@RequestMapping时，进行的逻辑处理类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-22 <br>
 * <br>
 */
public class ControllerAnnotationAction implements AnnotationClassAction,
		AnnotationMethodAction {

	private MappingModelManager manager;

	public MappingModelManager getManager() {
		return manager;
	}

	public void setManager(MappingModelManager manager) {
		this.manager = manager;
	}

	public <T> void process(Class<T> clazz, Method method, Annotation annotation) {
		MappingClassModel model = manager.getMappingModelWithClass(clazz);
		model.addMethodModel(new MappingMethodModel(method, (RequestMapping) annotation));
	}

	public <T> void process(Class<T> clazz, Annotation annotation) {
		RequestMapping mapping = AnnotationUtils.findAnnotation(clazz,
				RequestMapping.class);
		MappingClassModel model = null;
		if (mapping != null) {
			model = new MappingClassModel(clazz, mapping);
		} else {
			model = new MappingClassModel(clazz, null);
		}
		manager.putMappingModel(clazz, model);
	}


}
