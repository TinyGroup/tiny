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

package org.tinygroup.jspengine.tagplugins.jstl;

import org.tinygroup.jspengine.compiler.tagplugin.*;

public final class If implements TagPlugin {

    public void doTag(TagPluginContext ctxt) {
	String condV = ctxt.getTemporaryVariableName();
	ctxt.generateJavaSource("boolean " + condV + "=");
	ctxt.generateAttribute("test");
	ctxt.generateJavaSource(";");
	if (ctxt.isAttributeSpecified("var")) {
	    String scope = "PageContext.PAGE_SCOPE";
	    if (ctxt.isAttributeSpecified("scope")) {
		String scopeStr = ctxt.getConstantAttribute("scope");
		if ("request".equals(scopeStr)) {
		    scope = "PageContext.REQUEST_SCOPE";
		} else if ("session".equals(scopeStr)) {
		    scope = "PageContext.SESSION_SCOPE";
		} else if ("application".equals(scopeStr)) {
		    scope = "PageContext.APPLICATION_SCOPE";
		}
	    }
	    ctxt.generateJavaSource("_jspx_page_context.setAttribute(");
	    ctxt.generateAttribute("var");
	    ctxt.generateJavaSource(", new Boolean(" + condV + ")," + scope + ");");
	}
	ctxt.generateJavaSource("if (" + condV + "){");
	ctxt.generateBody();
	ctxt.generateJavaSource("}");
    }
}
