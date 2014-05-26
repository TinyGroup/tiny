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
package org.tinygroup.imda.usermodel;

import java.io.IOException;
import java.io.Writer;

import org.tinygroup.context.Context;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.exception.IMdaRuntimeException;
import org.tinygroup.imda.processor.ModelViewProcessor;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.springutil.SpringUtil;

public class CaseModelViewProcessor implements ModelViewProcessor<CaseModel> {

	public void process(ModelRequestInfo modelRequestInfo, Context context,
			Writer writer) {
		try {
			ModelManager modelManager = SpringUtil.getBean("modelManager");
			CaseModel model = modelManager.getModel(modelRequestInfo
					.getModelId());
			writer.write("page:" + context.get(CaseModelParamBuilder.KEY));
			context.put(CaseModelParamBuilder.KEY,
					1 + (Integer) context.get(CaseModelParamBuilder.KEY));
		} catch (IOException e) {
			throw new IMdaRuntimeException(e);
		}
	}

}
