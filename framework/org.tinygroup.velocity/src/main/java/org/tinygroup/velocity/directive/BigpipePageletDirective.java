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

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.velocity.impl.VelocityHelperImpl;

public class BigpipePageletDirective extends Directive {

	private static final String PAGELET = "pagelet";
	private static  Logger logger = LoggerFactory
			.getLogger(BigpipePageletDirective.class);

	
	public String getName() {
		return PAGELET;
	}

	
	public int getType() {
		return LINE;
	}

	
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException {
		String path = node.jjtGetChild(0).value(context).toString();
		String pagelet = null;
		if (path.startsWith("/")) {// 如果是绝对路径
			pagelet = path;
		} else {
			String templatePath = node.getTemplateName();
			pagelet = templatePath.substring(0,
					templatePath.lastIndexOf('/') + 1) + path;
		}
		VelocityHelperImpl velocityHelperImpl = SpringUtil
				.getBean("velocityHelper");
		try {
			velocityHelperImpl
					.processBigpipeTempleate(context, writer, pagelet);
		} catch (Exception e) {
			logger.errorMessage(e.getMessage(), e);
			throw new ParseErrorException(e.getMessage());
		}
		return true;
	}

}
