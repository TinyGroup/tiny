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
package org.tinygroup.modelplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.plugin.Plugin;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 
 * 功能说明: 根据模型生成功能表、菜单表数据

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-19 <br>
 * <br>
 */
public class ModelPlugin extends AbstractConfiguration implements Plugin {
	
	private static final String MODELCOFIG_NODE_PATH = "model-config";
	private static final String MENU_BEAN_NAME = "menu";
	private static final String FUNCTION_BEAN_NAME = "function";
	private ModelManager manager;
	
	private BeanOperatorManager beanManager;
	

	public ModelManager getManager() {
		return manager;
	}

	public void setManager(ModelManager manager) {
		this.manager = manager;
	}
	

	public BeanOperatorManager getBeanManager() {
		return beanManager;
	}

	public void setBeanManager(BeanOperatorManager beanManager) {
		this.beanManager = beanManager;
	}

	public String getApplicationNodePath() {
		return MODELCOFIG_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void start() {

		Map<String, Object> models = manager.getModelContainer()
				.getModelInstances();
		for (Object model : models.values()) {
			if (model instanceof EntityModel) {
				EntityModel entityModel = (EntityModel) model;
				insertOperation(entityModel);
				insertView(entityModel);
			}

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertView(EntityModel entityModel) {
		List<View> views = entityModel.getViews();
		DBOperator operator = beanManager.getDbOperator(MENU_BEAN_NAME);
		if (views.size() > 0) {
			List<Bean> viewBeans = new ArrayList<Bean>();
			for (int i = 0; i < views.size(); i++) {
				View view = views.get(i);
				Bean viewBean = new Bean(MENU_BEAN_NAME);
				setBeanProperty(viewBean, "menuCode", view.getId());
				setBeanProperty(viewBean, "menuName", entityModel.getTitle()+view.getTitle());
				setBeanProperty(viewBean, "description", view.getDescription());
				setBeanProperty(viewBean, "menuType", entityModel.getName());
				setBeanProperty(viewBean, "orderNumber", i + 1);
				setBeanProperty(
						viewBean,
						"menuUrl",
						getUrl(view.getType(), entityModel.getId(),
								view.getId()));
				if (operator.getBeans(viewBean) == null) {
					viewBeans.add(viewBean);
				}
			}
			if(viewBeans.size()>0){
				operator.batchInsert(viewBeans);
			}
		}
	}

	private String getUrl(String operateType, String modelId, String operateId) {
		return "EntityModel_" + operateType + "_" + modelId + "_" + operateId
				+ ".modellet";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertOperation(EntityModel entityModel) {
		List<Operation> operations = entityModel.getOperations();
		if (operations.size() > 0) {
			List<Bean> opBeans = new ArrayList<Bean>();
			DBOperator operator = beanManager.getDbOperator(FUNCTION_BEAN_NAME);
			for (int i = 0; i < operations.size(); i++) {
				Operation operation = operations.get(i);
				Bean opBean = new Bean(FUNCTION_BEAN_NAME);
				setBeanProperty(opBean, "functionCode", operation.getId());
				setBeanProperty(opBean, "functionName", entityModel.getTitle()+operation.getTitle());
				setBeanProperty(opBean, "description",
						operation.getDescription());
				setBeanProperty(opBean, "functionType", entityModel.getName());
				setBeanProperty(
						opBean,
						"functionUrl",
						getUrl(operation.getType(), entityModel.getId(),
								operation.getId()));
				setBeanProperty(opBean, "orderNumber", i + 1);
				if (operator.getBeans(opBean) == null) {
					opBeans.add(opBean);
				}
			}
			if(opBeans.size()>0){
				operator.batchInsert(opBeans);
			}
		}
	}

	private void setBeanProperty(Bean bean, String propertyName, Object value) {
		if (value != null) {
			bean.setProperty(propertyName, value);
		}
	}

	public void stop() {

	}


}
