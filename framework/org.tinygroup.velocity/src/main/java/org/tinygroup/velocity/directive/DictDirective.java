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
package org.tinygroup.velocity.directive;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.tinygroup.context.Context;
import org.tinygroup.dict.Dict;
import org.tinygroup.dict.DictGroup;
import org.tinygroup.dict.DictItem;
import org.tinygroup.dict.DictManager;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.velocity.TinyVelocityContext;

/**
 * 字典项翻译
 * 
 * @author luoguo
 * 
 */
public class DictDirective extends Directive {

	private static final String DICT = "d";

	
	public String getName() {
		return DICT;
	}

	
	public int getType() {
		return LINE;
	}

	
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException {
		DictManager dictManager = SpringUtil.getBean("dictManager");

		String dictType = node.jjtGetChild(0).value(context).toString();
		String dictGroupName = node.jjtGetChild(1).value(context).toString();
		String dictItemValue = node.jjtGetChild(2).value(context).toString();

		VelocityContext velocityContext = (VelocityContext) context
				.getInternalUserContext();
		TinyVelocityContext tinyVelocityContext = (TinyVelocityContext) velocityContext
				.getChainedContext();
		Context webContext = tinyVelocityContext.getContext();
		Dict dict = dictManager.getDict(dictType, webContext);
		for (DictGroup dictGroup : dict.getDictGroupList()) {
			if (dictGroup.getName().equals(dictGroupName)
					&& dictGroup.getItemList() != null) {
				for (DictItem item : dictGroup.getItemList()) {
					if (item.getValue().equals(dictItemValue)) {
						writer.write(item.getText());
						return true;
					}
				}
			}
		}
		return false;
	}

}
