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
package org.tinygroup.imda;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.imda.test.util.ModelTestUtil;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.imda.usermodel.CaseModel;
import org.tinygroup.imda.usermodel.CaseModelParamBuilder;
import org.tinygroup.springutil.SpringUtil;

public class ModelManagerTest extends TestCase {

	ModelManager manager;

	public void setUp() {
		ModelTestUtil.init();
		manager = SpringUtil.getBean(ModelManager.MODELMANAGER_BEAN);
		manager.addModel(getCaseModel());
	}

	public void testLoader() {
		// assertEquals(BaseModelLoader.LoaderValue, ModelTestUtil.getValue());
	}

	ModelRequestInfo getModelRequestInfo() {
		ModelRequestInfo modelRequestInfo = new ModelRequestInfo();
		modelRequestInfo.setModelId("a");
		modelRequestInfo.setOperationType("processor1");
		modelRequestInfo.setStageName("stage1");
		return modelRequestInfo;
	}

	public void testProcessService() {
		Context context = ContextFactory.getContext();
		manager.processParameter(getModelRequestInfo(),"parameterBuilder", context);
		manager.processService(getModelRequestInfo(), context);
		assertEquals(11, context.get(CaseModelParamBuilder.KEY));
	}

	public void testProcessView() {
		Context context = ContextFactory.getContext();
		manager.processParameter(getModelRequestInfo(),"parameterBuilder", context);
		manager.processService(getModelRequestInfo(), context);
		manager.processView(getModelRequestInfo(), new StringWriter(), context);
		assertEquals(12, context.get(CaseModelParamBuilder.KEY));
	}

	public void testProcessParam() {
		Context context = ContextFactory.getContext();
		manager.processParameter(getModelRequestInfo(),"parameterBuilder", context);
		assertEquals(10, context.get(CaseModelParamBuilder.KEY));
	}

	public void testUpdate() {
		manager.updateModel(getCaseModel2());
		Context context = ContextFactory.getContext();
		manager.processParameter(getModelRequestInfo(),"parameterBuilder", context);
		assertEquals(10, context.get(CaseModelParamBuilder.KEY));
		manager.updateModel(getCaseModel());
		context.clear();
		manager.processParameter(getModelRequestInfo(),"parameterBuilder", context);
		assertEquals(10, context.get(CaseModelParamBuilder.KEY));
	}

	public void testRemove() {
		CaseModel caseModel = getCaseModel();
		manager.removeModel(caseModel);
		try{
			manager.getModel(caseModel.getId());
			fail();
		}catch (Exception e) {
		}
	}

	private CaseModel getCaseModel() {
		CaseModel model = new CaseModel();
		model.setPage(10);
		model.setId("a");
		return model;
	}

	private CaseModel getCaseModel2() {
		CaseModel model = new CaseModel();
		model.setPage(11);
		model.setId("a");
		return model;
	}

}
