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
import org.tinygroup.velocity.TinyVelocityContext;

public class VarDirective extends Directive {

	private static final String VAR = "var";

	
	public String getName() {
		return VAR;
	}

	
	public int getType() {
		return LINE;
	}

	
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException {
		Object key = node.jjtGetChild(0).value(context);
		if (key != null) {
			String var = key.toString();
			VelocityContext velocityContext = (VelocityContext) context
					.getInternalUserContext();
			TinyVelocityContext tinyVelocityContext = (TinyVelocityContext) velocityContext
					.getChainedContext();
			Context webContext = tinyVelocityContext.getContext();
			String value = webContext.get(var);
			if (value != null) {
				writer.write(value);
			}
		}
		return true;
	}
}
